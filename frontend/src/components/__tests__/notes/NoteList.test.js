import { describe, it, expect, vi } from "vitest";
import { mount } from "@vue/test-utils";
import NoteList from "@/components/notes/NoteList.vue";
import { useNotesStore } from "@/stores/notesStore";
import { useCategoriesStore } from "@/stores/categoriesStore";

vi.mock("@/stores/notesStore");
vi.mock("@/stores/categoriesStore");

describe("NoteList", () => {

  let notesStore;
  let categoriesStore;

  beforeEach(() => {
    notesStore = {
      notes: [],
      fetchNotes: vi.fn(),
      createNote: vi.fn(),
      updateNote: vi.fn(),
      deleteNote: vi.fn(),
      toggleArchive: vi.fn(),
    };

    categoriesStore = {
      categories: [],
      selectedCategories: [],
    };

    useNotesStore.mockReturnValue(notesStore);
    useCategoriesStore.mockReturnValue(categoriesStore);
  });

  const mountComponent = () => mount(NoteList);


  it("renders notes correctly", async () => {
    notesStore.notes = [
      { id: 1, title: "Note 1", content: "Content 1", categories: [] },
      { id: 2, title: "Note 2", content: "Content 2", categories: [] },
    ];

    const wrapper = mountComponent();

    expect(wrapper.findAll(".note-item")).toHaveLength(2);
    expect(wrapper.text()).toContain("Note 1");
    expect(wrapper.text()).toContain("Note 2");
  });


  it("filters notes based on selected categories", async () => {
    notesStore.notes = [
      {
        id: 1,
        title: "Note 1",
        content: "Content 1",
        categories: [{ id: 1, name: "Category 1" }],
      },
      {
        id: 2,
        title: "Note 2",
        content: "Content 2",
        categories: [{ id: 2, name: "Category 2" }],
      },
    ];

    categoriesStore.categories = [
      { id: 1, name: "Category 1" },
      { id: 2, name: "Category 2" },
    ];
    categoriesStore.selectedCategories = [1];

    const wrapper = mountComponent();

    expect(wrapper.findAll(".note-item")).toHaveLength(1);
    expect(wrapper.text()).toContain("Note 1");
    expect(wrapper.text()).not.toContain("Note 2");
  });


  it('opens add note modal when "Add Note" button is clicked', async () => {
    const wrapper = mountComponent();
    await wrapper.find("#add-note-button").trigger("click");
    expect(wrapper.vm.isModalVisible).toBe(true);
    expect(wrapper.vm.modalTitle).toBe("Add Note");
  });


  it("opens edit note modal when edit button is clicked", async () => {
    notesStore.notes = [
      { id: 1, title: "Note 1", content: "Content 1", categories: [] },
    ];

    const wrapper = mountComponent();
    await wrapper.find('.note-item [title="Edit note"]').trigger("click");
    expect(wrapper.vm.isModalVisible).toBe(true);
    expect(wrapper.vm.modalTitle).toBe("Edit Note");
    expect(wrapper.vm.currentNote).toEqual(notesStore.notes[0]);
  });


  it("toggles archive status when archive button is clicked", async () => {
    notesStore.notes = [
      { id: 1, title: "Note 1", content: "Content 1", categories: [] },
    ];

    const wrapper = mountComponent();
    await wrapper.find(".note-item button:last-child").trigger("click");
    expect(notesStore.toggleArchive).toHaveBeenCalledWith(1);
  });


  it("filters notes based on archive status", async () => {
    notesStore.notes = [
      { id: 1, title: "Active Note", content: "Content 1", archived: false },
      { id: 2, title: "Archived Note", content: "Content 2", archived: true },
    ];

    const wrapper = mountComponent();

    await wrapper.find("button:nth-child(2)").trigger("click");
    expect(wrapper.findAll(".note-item")).toHaveLength(1);
    expect(wrapper.text()).toContain("Active Note");
    expect(wrapper.text()).not.toContain("Archived Note");

    await wrapper.find("button:nth-child(3)").trigger("click");
    expect(wrapper.findAll(".note-item")).toHaveLength(1);
    expect(wrapper.text()).toContain("Archived Note");
    expect(wrapper.text()).not.toContain("Active Note");
  });


  it("displays message when no notes are available", async () => {
    const wrapper = mountComponent();
    expect(wrapper.text()).toContain("No notes available.");
  });

});
