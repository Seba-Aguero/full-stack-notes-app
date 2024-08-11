import { fileURLToPath, URL } from 'node:url'
import { resolve } from "path";

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  test: {
    globals: true,
    environment: "jsdom",
    setupFilesAfterEnv: [
      resolve(__dirname, "src", "components", "__tests__", "test-setup.js"),
    ],
  },
});
