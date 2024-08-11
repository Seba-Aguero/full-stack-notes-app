import { describe, it, expect, vi } from "vitest";
import { mount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import App from "@/App.vue";
import TheHeader from "@/components/ui/TheHeader.vue";
import CategoryList from "@/components/CategoryList.vue";
import NoteList from "@/components/notes/NoteList.vue";

vi.mock("./stores/categoriesStore");
vi.mock("./stores/notesStore");

describe("App", () => {

  let pinia;

  beforeEach(() => {
    pinia = createPinia();
    setActivePinia(pinia);
  });

  const mountComponent = () => mount(App);


  it("renders properly", () => {
    const wrapper = mountComponent();
    expect(wrapper.findComponent(TheHeader).exists()).toBe(true);
    expect(wrapper.findComponent(CategoryList).exists()).toBe(true);
    expect(wrapper.findComponent(NoteList).exists()).toBe(true);
  });


  it("toggles category list visibility", async () => {
    const wrapper = mountComponent();
    const header = wrapper.findComponent(TheHeader);

    expect(wrapper.vm.isCategoryListVisible).toBe(true);

    await header.vm.$emit("toggle-category-list");

    expect(wrapper.vm.isCategoryListVisible).toBe(false);
  });
});
