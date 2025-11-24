# 🔐 Variáveis de Ambiente

Este arquivo documenta as variáveis de ambiente necessárias para o projeto.

## 📋 Variáveis Disponíveis

### `VITE_API_BASE_URL`

**Descrição:** URL base da API do backend

**Valores:**
- **Desenvolvimento:** `http://localhost:8080`
- **Produção:** URL do seu backend deployado (ex: `https://api.exemplo.com`)

**Exemplo de arquivo `.env` (desenvolvimento local):**
```env
VITE_API_BASE_URL=http://localhost:8080
```

**Configuração no Vercel:**
1. Acesse Settings > Environment Variables
2. Adicione:
   - Key: `VITE_API_BASE_URL`
   - Value: `https://seu-backend.com`
   - Environments: Production, Preview, Development

## 📝 Notas Importantes

- Todas as variáveis de ambiente no Vite devem começar com `VITE_`
- Variáveis sem o prefixo `VITE_` não estarão disponíveis no código do cliente
- Após adicionar variáveis no Vercel, é necessário fazer um novo deploy
- Para desenvolvimento local, crie um arquivo `.env` na raiz do projeto `mfademo-frontend`

## 🚀 Exemplo de Uso

O código já está configurado para usar essas variáveis:

```javascript
// src/services/api.js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
```

Se a variável não estiver definida, o código usa `http://localhost:8080` como padrão.

