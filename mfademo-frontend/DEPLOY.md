# 🚀 Guia de Deploy no Vercel

Este guia explica como fazer o deploy do frontend no Vercel.

## 📋 Pré-requisitos

1. Conta no [Vercel](https://vercel.com) (gratuita)
2. Backend deployado e acessível via HTTPS
3. Repositório Git (GitHub, GitLab ou Bitbucket) - opcional, mas recomendado

## 🔧 Configuração

### Opção 1: Deploy via CLI do Vercel

1. **Instale a CLI do Vercel:**
   ```bash
   npm i -g vercel
   ```

2. **Navegue até a pasta do frontend:**
   ```bash
   cd mfademo-frontend
   ```

3. **Faça login no Vercel:**
   ```bash
   vercel login
   ```

4. **Execute o deploy:**
   ```bash
   vercel
   ```
   
   Siga as instruções:
   - Selecione o projeto (ou crie um novo)
   - Confirme as configurações
   - Aguarde o build e deploy

5. **Para produção:**
   ```bash
   vercel --prod
   ```

### Opção 2: Deploy via Dashboard do Vercel (Recomendado)

1. **Acesse [vercel.com](https://vercel.com)** e faça login

2. **Clique em "Add New Project"**

3. **Conecte seu repositório Git:**
   - Selecione GitHub, GitLab ou Bitbucket
   - Autorize o acesso
   - Selecione o repositório do projeto

4. **Configure o projeto:**
   - **Framework Preset:** Vite
   - **Root Directory:** `mfademo-frontend` (se o projeto estiver na raiz, deixe vazio)
   - **Build Command:** `npm run build` (já configurado automaticamente)
   - **Output Directory:** `dist` (já configurado automaticamente)
   - **Install Command:** `npm install` (já configurado automaticamente)

5. **Configure variáveis de ambiente:**
   - Clique em "Environment Variables"
   - Adicione:
     - **Key:** `VITE_API_BASE_URL`
     - **Value:** URL do seu backend (ex: `https://seu-backend.herokuapp.com`)
     - **Environments:** Production, Preview, Development

6. **Clique em "Deploy"**

## 🔐 Configuração de Variáveis de Ambiente

No dashboard do Vercel, vá em **Settings > Environment Variables** e adicione:

| Variável | Valor | Descrição |
|----------|-------|-----------|
| `VITE_API_BASE_URL` | `https://seu-backend.com` | URL completa do backend em produção |

**Exemplos de valores:**
- `https://api.exemplo.com`
- `https://seu-backend.herokuapp.com`
- `https://seu-backend.railway.app`

## ⚙️ Configuração do Backend

Certifique-se de que o backend está configurado para:

1. **CORS:** Aceitar requisições do domínio do Vercel
   ```java
   // No CorsConfig.java, adicione o domínio do Vercel
   configuration.setAllowedOrigins(List.of(
       "https://seu-projeto.vercel.app",
       "https://seu-dominio.com"
   ));
   ```

2. **HTTPS:** O backend deve estar acessível via HTTPS em produção

3. **Acessibilidade:** O backend deve estar publicamente acessível

## 🔄 Atualizações Automáticas

Se você conectou um repositório Git:

- **Push para `main`/`master`:** Deploy automático em produção
- **Push para outras branches:** Deploy automático em preview
- **Pull Requests:** Deploy automático de preview para testar

## 📝 Verificações Pós-Deploy

Após o deploy, verifique:

1. ✅ A aplicação carrega corretamente
2. ✅ O login funciona
3. ✅ As requisições para o backend estão funcionando
4. ✅ O CORS está configurado corretamente
5. ✅ As variáveis de ambiente estão configuradas

## 🐛 Troubleshooting

### Erro de CORS
- Verifique se o backend aceita requisições do domínio do Vercel
- Adicione o domínio do Vercel na lista de origens permitidas no backend

### Erro 404 em rotas
- O `vercel.json` já está configurado com rewrites para SPA
- Verifique se o arquivo está no repositório

### Variáveis de ambiente não funcionam
- Certifique-se de que as variáveis começam com `VITE_`
- Faça um novo deploy após adicionar variáveis
- Verifique se as variáveis estão configuradas para o ambiente correto (Production/Preview)

### Build falha
- Verifique os logs de build no dashboard do Vercel
- Certifique-se de que todas as dependências estão no `package.json`
- Verifique se o Node.js version está compatível

## 📚 Recursos

- [Documentação do Vercel](https://vercel.com/docs)
- [Vite no Vercel](https://vercel.com/guides/deploying-vite-apps-with-vercel)
- [Variáveis de Ambiente no Vercel](https://vercel.com/docs/concepts/projects/environment-variables)

