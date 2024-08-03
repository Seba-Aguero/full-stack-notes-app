import axios from "@/axios";

const categoriesService = {
  getAllCategories() {
    return axios.get("/categories");
  },

  createCategory(categoryData) {
    return axios.post("/categories", categoryData);
  },

  updateCategory(id, categoryData) {
    return axios.put(`/categories/${id}`, categoryData);
  },

  deleteCategory(id) {
    return axios.delete(`/categories/${id}`);
  },
};

export default categoriesService;
