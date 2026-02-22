import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      apis:       fileURLToPath(new URL('./src/apis', import.meta.url)),
      assets:     fileURLToPath(new URL('./src/assets', import.meta.url)),
      components: fileURLToPath(new URL('./src/components', import.meta.url)),
      constant:   fileURLToPath(new URL('./src/constant', import.meta.url)),
      hooks:      fileURLToPath(new URL('./src/hooks', import.meta.url)),
      layouts:    fileURLToPath(new URL('./src/layouts', import.meta.url)),
      mocks:      fileURLToPath(new URL('./src/mocks', import.meta.url)),
      stores:     fileURLToPath(new URL('./src/stores', import.meta.url)),
      types:      fileURLToPath(new URL('./src/types', import.meta.url)),
      utils:      fileURLToPath(new URL('./src/utils', import.meta.url)),
      views:      fileURLToPath(new URL('./src/views', import.meta.url)),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:4000',
        changeOrigin: true,
      },
    },
  },
});
