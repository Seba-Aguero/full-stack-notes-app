<template>
  <main
    class="m-3 w-full rounded-lg bg-zinc-100 p-4"
  >
    <h2 class="mb-4 text-2xl font-semibold">Notes</h2>
    <div class="mb-4 flex justify-between">
      <button
        id="add-note-button"
        @click="openAddModal"
        class="rounded-lg bg-black p-2 text-white transition-colors hover:bg-zinc-700"
      >
        Add Note
      </button>
      <div id="archive-filter-buttons">
        <button
          @click="setFilter('all')"
          :class="{
            'bg-gray-300': filter === 'all',
            'hover:bg-gray-200': filter !== 'all',
          }"
          class="mx-1 rounded-lg p-2 transition-colors"
        >
          All
        </button>
        <button
          @click="setFilter('active')"
          :class="{
            'bg-gray-300': filter === 'active',
            'hover:bg-gray-200': filter !== 'active',
          }"
          class="mx-1 rounded-lg p-2 transition-colors"
        >
          Active
        </button>
        <button
          @click="setFilter('archived')"
          :class="{
            'bg-gray-300': filter === 'archived',
            'hover:bg-gray-200': filter !== 'archived',
          }"
          class="mx-1 rounded-lg p-2 transition-colors"
        >
          Archived
        </button>
      </div>
    </div>
    <div v-if="filteredNotes.length">
      <div
        v-for="note in filteredNotes"
        :key="note.id"
        class="note-item mb-4 rounded-lg border-2 border-gray-200 p-4 shadow-sm shadow-gray-200 transition-colors"
        :class="{ 'bg-gray-200': note.archived, 'bg-white': !note.archived }"
      >
        <div class="flex items-center justify-between">
          <div>
            <h3 class="text-xl font-bold">{{ note.title }}</h3>
          </div>
          <div class="flex space-x-4">
            <button
              @click="openEditModal(note)"
              class="rounded-lg bg-none text-black"
              title="Edit note"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-10 rounded-lg transition-colors hover:bg-zinc-300"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
                />
              </svg>
            </button>
            <button
              @click="deleteNote(note.id)"
              class="rounded-lg bg-none text-black"
              title="Delete category"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-10 rounded-lg transition-colors hover:bg-zinc-300"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"
                />
              </svg>
            </button>
            <button
              @click="toggleArchive(note.id)"
              class="w-full rounded-lg bg-black p-2 text-white transition-colors hover:bg-zinc-700"
            >
              {{ note.archived ? "Unarchive" : "Archive" }}
            </button>
          </div>
        </div>
        <p>{{ note.content }}</p>
        <div class="mt-2">
          <span
            v-for="category in note.categories"
            :key="category.id"
            class="mr-2 inline-block rounded-lg bg-gray-300 px-2 py-1 text-gray-700"
          >
            {{ category.name }}
          </span>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No notes available.</p>
    </div>
    <Modal
      :isVisible="isModalVisible"
      :modalTitle="modalTitle"
      :categories="categories"
      :noteData="currentNote"
      @close="closeModal"
      @save="saveNote"
    />
  </main>
</template>

<script setup>
import { ref, computed } from "vue";
import { useNotesStore } from '@/stores/notesStore'
import { useCategoriesStore } from '@/stores/categoriesStore'
import Modal from "./NoteModal.vue";
import Swal from "sweetalert2";

const notesStore = useNotesStore()
const categoriesStore = useCategoriesStore()

const notes = computed(() => notesStore.notes)
const categories = computed(() => categoriesStore.categories)
const selectedCategories = computed(() => categoriesStore.selectedCategories)

const isModalVisible = ref(false);
const modalTitle = ref("");
const currentNote = ref(null);
const filter = ref("all");

const filteredNotes = computed(() => {
  let filteredNotes = notes.value;

  if (selectedCategories.value.length > 0) {
    filteredNotes = filteredNotes.filter((note) =>
      note.categories.some((category) =>
        selectedCategories.value.includes(category.id)
      )
    );
  }

  if (filter.value === "archived") {
    filteredNotes = filteredNotes.filter((note) => note.archived);
  } else if (filter.value === "active") {
    filteredNotes = filteredNotes.filter((note) => !note.archived);
  }

  return filteredNotes;
});

const openAddModal = () => {
  modalTitle.value = "Add Note";
  currentNote.value = null;
  isModalVisible.value = true;
};

const openEditModal = (note) => {
  modalTitle.value = "Edit Note";
  currentNote.value = { ...note };
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
};

const saveNote = async (noteData) => {
  if (noteData.id) {
    await notesStore.updateNote(noteData);
  } else {
    await notesStore.createNote(noteData);
  }
};

const deleteNote = (noteId) => {
  Swal.fire({
    title: "Are you sure?",
    text: "You won't be able to revert this!",
    icon: "warning",
    reverseButtons: true,
    showCancelButton: true,
    confirmButtonColor: "black",
    cancelButtonColor: "#9ca3af",
    confirmButtonText: "Yes, delete it!",
  }).then(async (result) => {
    if (result.isConfirmed) {
      await notesStore.deleteNote(noteId);
      Swal.fire({
        title: "Deleted!",
        text: "Your note has been deleted.",
        icon: "success",
        confirmButtonColor: "black",
      });
    }
  });
};

const toggleArchive = async (noteId) => {
  await notesStore.toggleArchive(noteId);
};

const setFilter = (value) => {
  filter.value = value;
};

</script>
