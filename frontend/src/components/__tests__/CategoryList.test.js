import { describe, it, expect, vi } from "vitest";
import { mount } from "@vue/test-utils";
import CategoryList from "@/components/CategoryList.vue";
import { useCategoriesStore } from "@/stores/categoriesStore";

vi.mock("@/stores/categoriesStore");

vi.mock("sweetalert2", () => ({
  default: {
    fire: vi.fn().mockResolvedValue({ isConfirmed: true }),
  },
}));

describe("CategoryList", () => {
  let categoriesStore;

  beforeEach(() => {
    categoriesStore = {
      categories: [],
      selectedCategories: [],
      createCategory: vi.fn(),
      updateCategory: vi.fn(),
      deleteCategory: vi.fn(),
      setSelectedCategories: vi.fn(),
    };

    useCategoriesStore.mockReturnValue(categoriesStore);
  });

  const mountComponent = () => mount(CategoryList);

  it("renders categories correctly", () => {
    categoriesStore.categories = [
      { id: 1, name: "Category 1" },
      { id: 2, name: "Category 2" },
    ];

    const wrapper = mountComponent();

    expect(wrapper.findAll("li")).toHaveLength(2);
    expect(wrapper.text()).toContain("Category 1");
    expect(wrapper.text()).toContain("Category 2");
  });

  it("adds a new category", async () => {
    const wrapper = mountComponent();

    await wrapper
      .find('input[placeholder="New Category"]')
      .setValue("New Category");
    await wrapper.find("form").trigger("submit");

    expect(categoriesStore.createCategory).toHaveBeenCalledWith("New Category");
  });

  it("updates a category", async () => {
    categoriesStore.categories = [{ id: 1, name: "Old Category" }];
    const wrapper = mountComponent();

    await wrapper.find('button[title="Edit category"]').trigger("click");
    await wrapper.find('input[type="text"]').setValue("Updated Category");
    await wrapper.find('button[title="Save changes"]').trigger("click");

    expect(categoriesStore.updateCategory).toHaveBeenCalledWith({
      id: 1,
      name: "Updated Category",
    });
  });

  it("deletes a category", async () => {
    categoriesStore.categories = [{ id: 1, name: "Category to Delete" }];
    const wrapper = mountComponent();

    await wrapper.find('button[title="Delete category"]').trigger("click");

    expect(categoriesStore.deleteCategory).toHaveBeenCalledWith(1);
  });

  it("disables the form submission when the new category name is empty", async () => {
    const wrapper = mountComponent();

    await wrapper.find('input[placeholder="New Category"]').setValue("");
    await wrapper.find("form").trigger("submit");

    expect(categoriesStore.createCategory).not.toHaveBeenCalled();
  });
});
