<template>
  <div
    v-if="isVisible"
    class="fixed inset-0 z-50 flex items-center justify-center bg-gray-900 bg-opacity-50"
  >
    <div class="w-1/3 rounded-lg bg-white p-6 shadow-lg">
      <h3 class="mb-4 text-xl font-semibold">{{ modalTitle }}</h3>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label for="title" class="mb-2 block text-gray-700">Title</label>
          <input
            v-model="noteTitle"
            id="title"
            type="text"
            class="w-full rounded-lg border p-2"
            required
          />
        </div>
        <div class="mb-4">
          <label for="content" class="mb-2 block text-gray-700">Content</label>
          <textarea
            v-model="noteContent"
            id="content"
            rows="4"
            class="w-full rounded-lg border p-2"
            required
          ></textarea>
        </div>
        <div class="mb-4">
          <label class="mb-2 block text-gray-700">Categories</label>
          <div>
            <label
              v-for="category in categories"
              :key="category.id"
              class="mr-2 inline-flex items-center"
            >
              <input
                type="checkbox"
                class="mr-1 my-1 h-5 w-5 accent-black"
                :value="category"
                v-model="selectedCategories"
              />
              <span class="ml-1 mr-2">{{ category.name }}</span>
            </label>
          </div>
        </div>
        <div class="flex justify-end">
          <button
            type="button"
            @click="closeModal"
            class="mr-2 rounded-lg bg-gray-500 p-2 text-white transition-colors hover:bg-gray-600"
          >
            Cancel
          </button>
          <button type="submit" class="rounded-lg bg-black p-2 text-white transition-colors hover:bg-zinc-800">
            Save
          </button>
        </div>
      </form>
    </div>
  </div>
</template>


<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  isVisible: Boolean,
  modalTitle: String,
  categories: Array,
  noteData: Object,
});

const emit = defineEmits(["close", "save"]);

const noteTitle = ref("");
const noteContent = ref("");
const selectedCategories = ref([]);

watch(
  () => props.noteData,
  (newValue) => {
    if (newValue) {
      noteTitle.value = newValue.title || "";
      noteContent.value = newValue.content || "";
      selectedCategories.value = Array.isArray(newValue.categories)
        ? [...newValue.categories]
        : [];
    } else {
      noteTitle.value = "";
      noteContent.value = "";
      selectedCategories.value = [];
    }
  },
);

const closeModal = () => {
  emit("close");
  noteTitle.value = "";
  noteContent.value = "";
  selectedCategories.value = [];
};

const submitForm = () => {
  const noteData = {
    id: props.noteData?.id,
    title: noteTitle.value,
    content: noteContent.value,
    categories: selectedCategories.value,
  };
  emit("save", noteData);
  closeModal();
};
</script>
