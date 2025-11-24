# Frontend - Sistema Zero Trust com MFA

Frontend React para o sistema de demonstração Zero Trust com RBAC e MFA.

## 🚀 Como Executar

### Pré-requisitos
- Node.js 18+ e npm

### Instalação e Execução

```bash
# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm run dev
```

A aplicação estará disponível em: `http://localhost:5173`

## 📋 Funcionalidades

- **Login com MFA**: Autenticação em duas etapas
- **Dashboard**: Visualização de informações do usuário
- **Perfil**: Visualização e edição do perfil
- **Relatórios**: Acesso a relatórios (MANAGER/ADMIN)
- **Painel Admin**: Gerenciamento de usuários (ADMIN)
- **Logs de Auditoria**: Visualização de logs de acesso

## 🎨 Estrutura do Projeto

```
src/
├── components/          # Componentes React
│   ├── Login.jsx       # Tela de login
│   ├── MfaVerification.jsx  # Verificação MFA
│   ├── Dashboard.jsx   # Dashboard principal
│   ├── Profile.jsx     # Perfil do usuário
│   ├── Reports.jsx     # Relatórios
│   ├── AdminPanel.jsx  # Painel administrativo
│   ├── Logs.jsx        # Logs de auditoria
│   └── Navigation.jsx  # Navegação
├── context/            # Contextos React
│   └── AuthContext.jsx # Contexto de autenticação
├── services/           # Serviços
│   └── api.js          # Cliente API
└── App.jsx             # Componente principal
```

## 🔗 Integração com Backend

O frontend está configurado para se comunicar com o backend. A URL da API é configurada através da variável de ambiente `VITE_API_BASE_URL`.

**Desenvolvimento local:**
- Por padrão, usa `http://localhost:8080`
- Ou configure no arquivo `.env`: `VITE_API_BASE_URL=http://localhost:8080`

**Produção:**
- Configure a variável de ambiente `VITE_API_BASE_URL` no Vercel com a URL do seu backend deployado

Certifique-se de que o backend está rodando antes de iniciar o frontend.

## 👥 Usuários de Teste

- **admin** / admin123 (ADMIN)
- **manager** / manager123 (MANAGER)
- **user** / user123 (USER)

## 🚀 Deploy no Vercel

### Pré-requisitos
- Conta no [Vercel](https://vercel.com)
- Backend deployado e acessível via HTTPS

### Passos para Deploy

1. **Instale a CLI do Vercel (opcional):**
   ```bash
   npm i -g vercel
   ```

2. **Faça login no Vercel:**
   ```bash
   vercel login
   ```

3. **Deploy do projeto:**
   ```bash
   cd mfademo-frontend
   vercel
   ```
   
   Ou conecte seu repositório GitHub/GitLab no dashboard do Vercel.

4. **Configure a variável de ambiente:**
   - Acesse o dashboard do Vercel
   - Vá em Settings > Environment Variables
   - Adicione: `VITE_API_BASE_URL` com a URL do seu backend
   - Exemplo: `https://seu-backend.herokuapp.com` ou `https://api.seudominio.com`

5. **Redeploy:**
   - Após adicionar a variável de ambiente, faça um novo deploy
   - O Vercel detecta automaticamente mudanças no repositório conectado

### Configuração Automática

O projeto já está configurado com `vercel.json` que:
- Define o framework como Vite
- Configura rewrites para SPA (Single Page Application)
- Otimiza cache de assets estáticos

### Variáveis de Ambiente no Vercel

No dashboard do Vercel, configure:
- **VITE_API_BASE_URL**: URL completa do backend (ex: `https://api.exemplo.com`)

**Importante:** 
- Use HTTPS para o backend em produção
- Configure CORS no backend para aceitar requisições do domínio do Vercel
- O backend deve estar acessível publicamente

## 📝 Notas

- O código MFA é exibido no console do servidor backend
- O token JWT é armazenado no localStorage
- A navegação é adaptada baseada no role do usuário
- Para produção, certifique-se de que o backend tem CORS configurado corretamente
