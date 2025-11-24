# 🧪 Guia de Testes da API - Zero Trust MFA Demo

## 📋 Índice
1. [Autenticação](#autenticação)
2. [Endpoints de Usuário](#endpoints-de-usuário)
3. [Endpoints de Gestor](#endpoints-de-gestor)
4. [Endpoints de Admin](#endpoints-de-admin)
5. [Logs de Auditoria](#logs-de-auditoria)
6. [Cenários de Teste](#cenários-de-teste)

---

## 🔐 Autenticação

### 1. Login (sem MFA)
**Endpoint:** `POST /auth/login`

**Body:**
```json
{
  "username": "vazado",
  "password": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "mfaRequired": false,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tmpToken": null,
  "role": "USER",
  "message": "Login realizado com sucesso"
}
```

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"vazado","password":"senha123"}'
```

---

### 2. Login (com MFA)
**Endpoint:** `POST /auth/login`

**Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Resposta (200 OK):**
```json
{
  "mfaRequired": true,
  "token": null,
  "tmpToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "role": "ADMIN",
  "message": "MFA requerido. Verifique o código no console."
}
```

**⚠️ IMPORTANTE:** Verifique o console do servidor para ver o código MFA:
```
========================================
=== MFA CODE for admin : 123456 ===
========================================
```

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

### 3. Verificar MFA
**Endpoint:** `POST /auth/mfa/verify`

**Body:**
```json
{
  "tmpToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "code": "123456"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "MFA verificado com sucesso"
}
```

**Resposta (401 Unauthorized) - Código inválido:**
```json
{
  "error": "Código MFA inválido",
  "message": "Código MFA inválido"
}
```

**Exemplo cURL:**
```bash
curl -X POST http://localhost:8080/auth/mfa/verify \
  -H "Content-Type: application/json" \
  -d '{"tmpToken":"a1b2c3d4-e5f6-7890-abcd-ef1234567890","code":"123456"}'
```

---

## 👤 Endpoints de Usuário

**⚠️ Todos requerem header:** `Authorization: Bearer {token}`

### 4. Obter Perfil
**Endpoint:** `GET /api/user/profile`

**Headers:**
```
Authorization: Bearer {seu-token-jwt}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "username": "user",
  "role": "USER",
  "enabled": true,
  "mfaEnabled": true,
  "email": "user@example.com"
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 5. Dashboard do Usuário
**Endpoint:** `GET /api/user/dashboard`

**Headers:**
```
Authorization: Bearer {seu-token-jwt}
```

**Resposta (200 OK):**
```json
{
  "username": "user",
  "role": "USER",
  "message": "Bem-vindo ao dashboard! Você tem acesso como USER",
  "permissions": {
    "viewOwnProfile": true,
    "viewReports": false,
    "viewAdminPanel": false,
    "manageUsers": false
  }
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/user/dashboard \
  -H "Authorization: Bearer {seu-token-jwt}"
```

---

## 📊 Endpoints de Gestor

**⚠️ Requer role:** MANAGER ou ADMIN

### 6. Relatórios
**Endpoint:** `GET /api/manager/reports`

**Headers:**
```
Authorization: Bearer {token-manager-ou-admin}
```

**Resposta (200 OK):**
```json
{
  "message": "Relatórios de Gestão",
  "username": "manager",
  "role": "MANAGER",
  "reports": [
    {
      "id": 1,
      "name": "Relatório de Vendas",
      "status": "disponível"
    },
    {
      "id": 2,
      "name": "Relatório de Performance",
      "status": "disponível"
    },
    {
      "id": 3,
      "name": "Relatório de Equipe",
      "status": "disponível"
    }
  ]
}
```

**Resposta (403 Forbidden) - Se não for MANAGER/ADMIN:**
```json
{
  "error": "Acesso negado",
  "message": "Apenas gestores e administradores podem acessar relatórios"
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/manager/reports \
  -H "Authorization: Bearer {token-manager}"
```

---

### 7. Dashboard de Gestão
**Endpoint:** `GET /api/manager/dashboard`

**Headers:**
```
Authorization: Bearer {token-manager-ou-admin}
```

**Resposta (200 OK):**
```json
{
  "message": "Dashboard de Gestão",
  "username": "manager",
  "role": "MANAGER",
  "features": [
    "Visualizar relatórios",
    "Gerenciar equipe",
    "Acessar métricas de performance"
  ]
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/manager/dashboard \
  -H "Authorization: Bearer {token-manager}"
```

---

## 🔧 Endpoints de Admin

**⚠️ Requer role:** ADMIN apenas

### 8. Painel Administrativo
**Endpoint:** `GET /api/admin/panel`

**Headers:**
```
Authorization: Bearer {token-admin}
```

**Resposta (200 OK):**
```json
{
  "message": "Painel Administrativo",
  "username": "admin",
  "features": [
    "Gerenciar usuários",
    "Visualizar todos os logs",
    "Configurar sistema",
    "Acessar relatórios completos"
  ]
}
```

**Resposta (403 Forbidden) - Se não for ADMIN:**
```json
{
  "error": "Acesso negado",
  "message": "Apenas administradores podem acessar este painel"
}
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/admin/panel \
  -H "Authorization: Bearer {token-admin}"
```

---

### 9. Listar Todos os Usuários
**Endpoint:** `GET /api/admin/users`

**Headers:**
```
Authorization: Bearer {token-admin}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "role": "ADMIN",
    "enabled": true,
    "mfaEnabled": true,
    "email": "admin@example.com"
  },
  {
    "id": 2,
    "username": "manager",
    "role": "MANAGER",
    "enabled": true,
    "mfaEnabled": true,
    "email": "manager@example.com"
  },
  {
    "id": 3,
    "username": "user",
    "role": "USER",
    "enabled": true,
    "mfaEnabled": true,
    "email": "user@example.com"
  }
]
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer {token-admin}"
```

---

## 📝 Logs de Auditoria

### 10. Ver Todos os Logs (ADMIN apenas)
**Endpoint:** `GET /api/logs`

**Headers:**
```
Authorization: Bearer {token-admin}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "endpoint": "/auth/login",
    "success": true,
    "message": "Login bem-sucedido (sem MFA)",
    "timestamp": "2024-01-15T16:03:45",
    "ipAddress": "127.0.0.1"
  },
  {
    "id": 2,
    "username": "user",
    "endpoint": "/api/admin/panel",
    "success": false,
    "message": "Tentativa de acesso negada - usuário não é ADMIN",
    "timestamp": "2024-01-15T16:05:12",
    "ipAddress": "127.0.0.1"
  }
]
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/logs \
  -H "Authorization: Bearer {token-admin}"
```

---

### 11. Ver Meus Logs
**Endpoint:** `GET /api/logs/my`

**Headers:**
```
Authorization: Bearer {seu-token-jwt}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "username": "user",
    "endpoint": "/api/user/profile",
    "success": true,
    "message": "Perfil acessado com sucesso",
    "timestamp": "2024-01-15T16:03:45",
    "ipAddress": "127.0.0.1"
  }
]
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/logs/my \
  -H "Authorization: Bearer {seu-token-jwt}"
```

---

## 🎭 Cenários de Teste Completos

### Cenário 1: Login Completo com MFA (ADMIN)

```bash
# 1. Fazer login
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

# 2. Extrair tmpToken (ou ver no console do servidor)
TMP_TOKEN=$(echo $RESPONSE | jq -r '.tmpToken')

# 3. Verificar código MFA no console do servidor
# Exemplo: código = 123456

# 4. Verificar MFA
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:8080/auth/mfa/verify \
  -H "Content-Type: application/json" \
  -d "{\"tmpToken\":\"$TMP_TOKEN\",\"code\":\"123456\"}")

# 5. Extrair token final
TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.token')

# 6. Acessar painel admin
curl -X GET http://localhost:8080/api/admin/panel \
  -H "Authorization: Bearer $TOKEN"
```

---

### Cenário 2: Tentativa de Acesso Negado (RBAC)

```bash
# 1. Login como USER (sem MFA para facilitar)
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usernomfa","password":"senha123"}')

TOKEN=$(echo $RESPONSE | jq -r '.token')

# 2. Tentar acessar painel admin (deve retornar 403)
curl -X GET http://localhost:8080/api/admin/panel \
  -H "Authorization: Bearer $TOKEN"

# Resposta esperada: 403 Forbidden
```

---

### Cenário 3: Demonstração de Ataque (Credencial Vazada)

```bash
# 1. Atacante usa credencial vazada (SEM MFA)
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"vazado","password":"senha123"}')

TOKEN=$(echo $RESPONSE | jq -r '.token')

# 2. Atacante consegue acessar recursos do usuário
curl -X GET http://localhost:8080/api/user/dashboard \
  -H "Authorization: Bearer $TOKEN"

# Resultado: Acesso bem-sucedido (vulnerabilidade demonstrada)
```

---

### Cenário 4: Verificar Logs de Auditoria

```bash
# 1. Login como ADMIN
ADMIN_RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

# ... (processo MFA) ...

# 2. Ver todos os logs
curl -X GET http://localhost:8080/api/logs \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'

# 3. Ver apenas meus logs
curl -X GET http://localhost:8080/api/logs/my \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
```

---

## 📋 Usuários de Teste

| Username | Senha | Role | MFA | Uso |
|----------|-------|------|-----|-----|
| `admin` | `admin123` | ADMIN | ✅ ON | Testar acesso total |
| `manager` | `manager123` | MANAGER | ✅ ON | Testar relatórios |
| `user` | `user123` | USER | ✅ ON | Testar acesso básico |
| `vazado` | `senha123` | USER | ❌ OFF | **Demo de ataque** |
| `usernomfa` | `senha123` | USER | ❌ OFF | Teste sem MFA |

---

## 🛠️ Ferramentas Recomendadas

### Postman
1. Importar coleção (criar manualmente com os endpoints acima)
2. Configurar variável `{{token}}` para reutilizar tokens
3. Criar ambiente com `base_url = http://localhost:8080`

### cURL
- Usar os exemplos acima
- Salvar token em variável: `TOKEN="seu-token-aqui"`

### HTTPie
```bash
# Instalar: pip install httpie

# Login
http POST localhost:8080/auth/login username=admin password=admin123

# Com token
http GET localhost:8080/api/user/profile Authorization:"Bearer {token}"
```

### Insomnia / Thunder Client
- Similar ao Postman
- Interface gráfica amigável

---

## ⚠️ Notas Importantes

1. **Token JWT:** Válido por 24 horas (86400000ms)
2. **MFA Code:** Exibido no console do servidor, válido até ser usado
3. **Logs:** Todos os acessos são registrados automaticamente
4. **CORS:** Configurado para aceitar qualquer origem (apenas para demo)

---

## 🐛 Troubleshooting

### Erro 401 Unauthorized
- Verificar se o token está no header: `Authorization: Bearer {token}`
- Verificar se o token não expirou
- Verificar se o token está completo (não truncado)

### Erro 403 Forbidden
- Verificar se o usuário tem a role necessária
- Verificar logs de auditoria para ver a tentativa negada

### MFA Code não encontrado
- Verificar console do servidor Spring Boot
- O código é exibido imediatamente após login com MFA habilitado

---

**Bons testes! 🚀**

