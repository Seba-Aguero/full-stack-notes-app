import { defineStore } from "pinia";
import categoriesService from "@/services/categoriesService";

export const useCategoriesStore = defineStore("categories", {
  state: () => ({
    categories: [],
    selectedCategories: [],
  }),
  actions: {
    async fetchCategories() {
      try {
        const response = await categoriesService.getAllCategories();
        this.categories = response.data;
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    },
    async addCategory(categoryName) {
      try {
        const response = await categoriesService.createCategory({
          name: categoryName,
        });
        this.categories.push(response.data);
      } catch (error) {
        console.error("Error adding category:", error);
      }
    },
    async updateCategory(categoryData) {
      try {
        const response = await categoriesService.updateCategory(
          categoryData.id,
          categoryData,
        );
        const index = this.categories.findIndex(
          (category) => category.id === categoryData.id,
        );
        if (index !== -1) {
          this.categories[index] = response.data;
        }
      } catch (error) {
        console.error("Error updating category:", error);
      }
    },
    async deleteCategory(categoryId) {
      try {
        await categoriesService.deleteCategory(categoryId);
        this.categories = this.categories.filter(
          (category) => category.id !== categoryId,
        );
        this.selectedCategories = this.selectedCategories.filter(
          (id) => id !== categoryId,
        );
      } catch (error) {
        console.error("Error deleting category:", error);
      }
    },
    setSelectedCategories(categories) {
      this.selectedCategories = categories;
    },
  },
});