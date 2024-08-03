<template>
  <header class="bg-black p-4 text-white h-16">
    <div class="flex space-x-4 lg:space-x-0 ms-2 ms-lg-3">
      <button @click="toggleCategoryList" class="text-xl lg:text-2xl lg:hidden">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="size-9 rounded-lg hover:bg-zinc-800"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
          />
        </svg>
      </button>
      <h1 class="text-2xl font-bold lg:text-2xl">Notes App</h1>
    </div>
  </header>
</template>

<script setup>
const emit = defineEmits(["toggle-category-list"]);

const toggleCategoryList = () => {
  emit("toggle-category-list");
};
</script>
