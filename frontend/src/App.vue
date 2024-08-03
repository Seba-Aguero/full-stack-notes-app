<template>
  <div class="overflow-y- overflow-x-hidden bg-white">
    <TheHeader @toggle-category-list="toggleCategoryList" />
    <div class="flex flex-col justify-evenly lg:flex-row">
      <CategoryList
        class="mb-4 w-full lg:mb-0 lg:w-[30%] xl:w-[25%]"
        :isVisible="isCategoryListVisible"
      />
      <NoteList class="w-full lg:w-[70%] xl:w-[75%]" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import TheHeader from "@/components/ui/TheHeader.vue";
import CategoryList from "@/components/CategoryList.vue";
import NoteList from "@/components/notes/NoteList.vue";
import { useCategoriesStore } from "./stores/categoriesStore";
import { useNotesStore } from "./stores/notesStore";

const isCategoryListVisible = ref(true);

const categoriesStore = useCategoriesStore();
const notesStore = useNotesStore();

onMounted(async () => {
  await Promise.all([
    notesStore.fetchNotes(),
    categoriesStore.fetchCategories(),
  ]);
});

const toggleCategoryList = () => {
  isCategoryListVisible.value = !isCategoryListVisible.value;
};

</script>
