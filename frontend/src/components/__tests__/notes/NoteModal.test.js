import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import NoteModal from "@/components/notes/NoteModal.vue";

describe("NoteModal", () => {
  const mountComponent = (props = {}) =>
    mount(NoteModal, {
      props: {
        isVisible: true,
        modalTitle: "Test Modal",
        categories: [],
        noteData: null,
        ...props,
      },
    });

  it("renders correctly when visible", () => {
    const wrapper = mountComponent();

    expect(wrapper.isVisible()).toBe(true);
    expect(wrapper.text()).toContain("Test Modal");
  });

  it("emits save event with correct data", async () => {
    const categories = [{ id: 1, name: "Category 1" }];
    const wrapper = mountComponent({ categories });

    const checkbox = wrapper.find('input[type="checkbox"]');
    expect(checkbox.exists()).toBe(true);

    await wrapper.find("#title").setValue("Test Note");
    await wrapper.find("#content").setValue("Test Content");
    await checkbox.setValue(true);

    await wrapper.find("form").trigger("submit");

    expect(wrapper.emitted().save[0][0]).toEqual({
      id: undefined,
      title: "Test Note",
      content: "Test Content",
      categories: [{ id: 1, name: "Category 1" }],
    });
  });

  it("closes modal when cancel button is clicked", async () => {
    const wrapper = mountComponent();
    await wrapper.find("#close-modal-button").trigger("click");
    expect(wrapper.emitted("close")).toBeTruthy();
  });

  it("renders all categories", () => {
    const multipleCategories = [
      { id: 1, name: "Category 1" },
      { id: 2, name: "Category 2" },
    ];
    const wrapper = mountComponent({ categories: multipleCategories });

    const checkboxes = wrapper.findAll('input[type="checkbox"]');
    expect(checkboxes.length).toBe(2);
    expect(wrapper.text()).toContain("Category 1");
    expect(wrapper.text()).toContain("Category 2");
  });
});
