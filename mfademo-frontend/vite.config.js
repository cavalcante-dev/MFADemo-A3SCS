import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Configuração para o Vercel
  build: {
    outDir: 'dist',
    sourcemap: false,
  },
  // Expõe variáveis de ambiente com prefixo VITE_
  envPrefix: 'VITE_',
})
