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

O frontend está configurado para se comunicar com o backend em `http://localhost:8080`.

Certifique-se de que o backend está rodando antes de iniciar o frontend.

## 👥 Usuários de Teste

- **admin** / admin123 (ADMIN)
- **manager** / manager123 (MANAGER)
- **user** / user123 (USER)

## 📝 Notas

- O código MFA é exibido no console do servidor backend
- O token JWT é armazenado no localStorage
- A navegação é adaptada baseada no role do usuário
