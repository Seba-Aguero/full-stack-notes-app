<template>
  <aside
    class="m-3 w-full overflow-y-auto rounded-lg bg-zinc-100 p-4"
    :class="{ 'hidden lg:block': !isVisible }"
  >
    <h2 class="mb-5 text-2xl font-semibold">Categories</h2>
    <ul class="space-y-6">
      <li
        v-for="category in categories"
        :key="category.id"
        class="flex items-center justify-between"
      >
        <div>
          <label class="flex items-center">
            <input
              type="checkbox"
              class="mr-2 h-5 w-5 accent-black"
              v-model="selectedCategories"
              :value="category.id"
            />
            <span v-if="!isEditing(category)">{{ category.name }}</span>
            <input
              v-else
              v-model="editCategoryName"
              class="rounded border p-2"
              @keyup.enter="saveCategory(category.id)"
            />
          </label>
        </div>
        <div class="flex space-x-4">
          <button
            @click="startEditing(category.id, category.name)"
            v-if="!isEditing(category)"
            title="Edit category"
          >
            <svg
              title="Edit category"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="size-6 rounded-lg transition-colors hover:bg-zinc-300"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
              />
            </svg>
          </button>
          <button
            @click="saveCategory(category.id)"
            v-if="isEditing(category)"
            title="Save changes"
          >
            <svg
              title="Save changes"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="size-6 rounded-lg transition-colors hover:bg-zinc-300"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m4.5 12.75 6 6 9-13.5"
              />
            </svg>
          </button>
          <button
            @click="deleteCategory(category.id)"
            v-if="!isEditing(category)"
            title="Delete category"
          >
            <svg
              title="Delete category"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="size-6 rounded-lg transition-colors hover:bg-zinc-300"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"
              />
            </svg>
          </button>
        </div>
      </li>
    </ul>
    <form @submit.prevent="createCategory" class="mt-5">
      <input
        v-model="newCategoryName"
        class="w-full rounded-lg border p-2"
        placeholder="New Category"
        required
      />
      <button
        type="submit"
        class="mt-2 w-full rounded-lg bg-black p-2 text-white transition-colors hover:bg-zinc-700"
        title="Add new category"
      >
        Add Category
      </button>
    </form>
  </aside>
</template>

<script setup>
import { ref, computed } from "vue";
import { useCategoriesStore } from "@/stores/categoriesStore";
import Swal from "sweetalert2";

const props = defineProps({
  isVisible: {
    type: Boolean,
    default: true,
  },
});

const categoriesStore = useCategoriesStore();

const newCategoryName = ref("");
const editingCategoryId = ref(null);
const editCategoryName = ref("");

const categories = computed(() => categoriesStore.categories);
const selectedCategories = computed({
  get: () => categoriesStore.selectedCategories,
  set: (value) => categoriesStore.setSelectedCategories(value),
});

const createCategory = async () => {
  if (newCategoryName.value.trim() !== "") {
    await categoriesStore.createCategory(newCategoryName.value.trim());
    newCategoryName.value = "";
    editingCategoryId.value = null;
  }
};

const saveCategory = async (categoryId) => {
  const currentCategory = categories.value.find(
    (category) => category.id === categoryId,
  );
  if (!currentCategory) return;

  const updatedName = editCategoryName.value.trim();
  if (updatedName === currentCategory.name) {
    editingCategoryId.value = null;
    editCategoryName.value = "";
    return;
  }

  await categoriesStore.updateCategory({ id: categoryId, name: updatedName });
  editingCategoryId.value = null;
  editCategoryName.value = "";
};

const deleteCategory = (categoryId) => {
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
      await categoriesStore.deleteCategory(categoryId);
      Swal.fire({
        title: "Deleted!",
        text: "Your category has been deleted.",
        icon: "success",
        confirmButtonColor: "black",
      });
    }
  });
};

const startEditing = (categoryId, categoryName) => {
  editingCategoryId.value = categoryId;
  editCategoryName.value = categoryName;
};

const isEditing = (category) => {
  return editingCategoryId.value === category.id;
};
</script>
