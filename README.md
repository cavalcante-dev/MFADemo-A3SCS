# 🛡️ Sistema Zero Trust com RBAC + MFA - Guia de Demonstração

## 📋 Visão Geral

Este sistema demonstra os conceitos de **Zero Trust**, **RBAC (Role-Based Access Control)** e **MFA (Multi-Factor Authentication)** em uma aplicação Spring Boot.

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven 3.6+

### Executar a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 👥 Usuários de Demonstração

O sistema cria automaticamente os seguintes usuários ao iniciar:

| Username | Senha | Role | MFA | Uso |
|----------|-------|------|-----|-----|
| `admin` | `admin123` | ADMIN | ✅ ON | Acesso total ao sistema |
| `manager` | `manager123` | MANAGER | ✅ ON | Acesso a relatórios e gestão |
| `user` | `user123` | USER | ✅ ON | Acesso básico |
| `vazado` | `senha123` | USER | ❌ OFF | **Credencial vazada para demo de ataque** |
| `usernomfa` | `senha123` | USER | ❌ OFF | Usuário sem MFA (comparação) |

## 📡 Endpoints da API

### Autenticação

#### 1. Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Resposta (com MFA):**
```json
{
  "mfaRequired": true,
  "tmpToken": "uuid-temporario",
  "role": "ADMIN",
  "message": "MFA requerido. Verifique o código no console."
}
```

**Resposta (sem MFA):**
```json
{
  "mfaRequired": false,
  "token": "jwt-token",
  "role": "USER",
  "message": "Login realizado com sucesso"
}
```

#### 2. Verificar MFA
```http
POST /auth/mfa/verify
Content-Type: application/json

{
  "tmpToken": "uuid-temporario",
  "code": "123456"
}
```

**Nota:** O código MFA é exibido no console do servidor quando o login é realizado.

**Resposta:**
```json
{
  "token": "jwt-token-final",
  "message": "MFA verificado com sucesso"
}
```

### Endpoints Protegidos

Todos os endpoints abaixo requerem o header:
```
Authorization: Bearer {jwt-token}
```

#### Perfil do Usuário
```http
GET /api/user/profile
```

#### Dashboard do Usuário
```http
GET /api/user/dashboard
```

#### Relatórios (MANAGER/ADMIN)
```http
GET /api/manager/reports
```

#### Dashboard de Gestão (MANAGER/ADMIN)
```http
GET /api/manager/dashboard
```

#### Painel Admin (ADMIN apenas)
```http
GET /api/admin/panel
```

#### Listar Usuários (ADMIN apenas)
```http
GET /api/admin/users
```

#### Logs de Auditoria
```http
GET /api/logs          # Todos os logs (ADMIN apenas)
GET /api/logs/my       # Meus logs (qualquer usuário autenticado)
```

## 🎭 Roteiro de Demonstração (20 minutos)

### Parte 1: Login Normal com MFA (5 min)

1. **Login como ADMIN:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

2. **Verificar código MFA no console do servidor:**
   ```
   === MFA CODE for admin : 123456 ===
   ```

3. **Verificar MFA:**
   ```bash
   curl -X POST http://localhost:8080/auth/mfa/verify \
     -H "Content-Type: application/json" \
     -d '{"tmpToken":"uuid-retornado","code":"123456"}'
   ```

4. **Acessar painel admin:**
   ```bash
   curl -X GET http://localhost:8080/api/admin/panel \
     -H "Authorization: Bearer {token}"
   ```

### Parte 2: Demonstração RBAC (5 min)

1. **Login como USER:**
   - Fazer login e verificar MFA
   - Tentar acessar `/api/admin/panel` → **403 Acesso Negado**
   - Verificar logs: `GET /api/logs/my`

2. **Login como MANAGER:**
   - Fazer login e verificar MFA
   - Acessar `/api/manager/reports` → ✅ Sucesso
   - Tentar acessar `/api/admin/panel` → **403 Acesso Negado**

3. **Login como ADMIN:**
   - Acessar `/api/admin/panel` → ✅ Sucesso
   - Acessar `/api/admin/users` → ✅ Sucesso
   - Acessar `/api/logs` → ✅ Sucesso

### Parte 3: Simulação de Ataque - Credencial Vazada (10 min)

#### Cenário 1: Ataque SEM MFA (Vulnerável)

1. **Atacante usa credencial vazada:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"vazado","password":"senha123"}'
   ```

2. **Resultado:** Login bem-sucedido SEM necessidade de MFA! ⚠️
   - O atacante obtém token JWT diretamente
   - Pode acessar recursos do usuário

3. **Demonstrar o acesso:**
   ```bash
   curl -X GET http://localhost:8080/api/user/dashboard \
     -H "Authorization: Bearer {token-obtido}"
   ```

#### Cenário 2: Ataque COM MFA (Protegido)

1. **Ativar MFA para o usuário (via banco ou código):**
   - Atualizar `mfaEnabled = true` para o usuário `vazado`

2. **Tentar ataque novamente:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"vazado","password":"senha123"}'
   ```

3. **Resultado:** Sistema requer MFA! ✅
   - Atacante não consegue obter token sem o código
   - Mesmo com credenciais corretas, acesso é bloqueado

4. **Verificar logs de auditoria:**
   ```bash
   curl -X GET http://localhost:8080/api/logs \
     -H "Authorization: Bearer {token-admin}"
   ```
   
   Logs mostrarão:
   - Tentativas de login
   - Tentativas de acesso negadas
   - Timestamp e IP de cada ação

## 📊 Logs de Auditoria

Todos os acessos são registrados automaticamente:

- ✅ **Sucesso:** Login bem-sucedido, acesso autorizado
- ❌ **Negado:** Tentativa de acesso sem permissão
- 🔍 **Rastreabilidade:** Username, endpoint, timestamp, IP

Exemplo de log:
```
[AUDIT] 2024-01-15T16:03:45 - ✗ NEGADO - user123 - /api/admin/panel - Tentativa de acesso negada - usuário não é ADMIN
```

## 🔐 Conceitos Demonstrados

### Zero Trust
- **Nunca confiar, sempre verificar:** Cada requisição é autenticada
- **Princípio do menor privilégio:** Usuários só acessam o necessário
- **Auditoria completa:** Todas as ações são registradas

### RBAC (Role-Based Access Control)
- **ADMIN:** Acesso total ao sistema
- **MANAGER:** Acesso a relatórios e gestão
- **USER:** Acesso apenas ao próprio perfil

### MFA (Multi-Factor Authentication)
- **Fator 1:** Algo que você sabe (senha)
- **Fator 2:** Algo que você tem (código MFA)
- **Proteção:** Mesmo com senha vazada, atacante não consegue acesso

## 🛠️ Tecnologias Utilizadas

- **Spring Boot 4.0**
- **Spring Security** (Autenticação e Autorização)
- **JWT** (JSON Web Tokens)
- **H2 Database** (Banco em memória)
- **JPA/Hibernate** (ORM)
- **Lombok** (Redução de boilerplate)

## 📝 Notas Importantes

1. **MFA Simulado:** O código MFA é exibido no console do servidor. Em produção, seria enviado via SMS/Email/App.
2. **Banco em Memória:** Os dados são perdidos ao reiniciar a aplicação.
3. **CORS Aberto:** Configurado para `*` apenas para demonstração. Em produção, especificar origens.
4. **JWT Secret:** Usar uma chave segura em produção (mínimo 256 bits).

## 🎯 Pontos de Destaque para Apresentação

1. ✅ **Autenticação em duas etapas** (MFA)
2. ✅ **Controle de acesso baseado em papéis** (RBAC)
3. ✅ **Logs de auditoria completos**
4. ✅ **Demonstração prática de vulnerabilidade** (credencial vazada)
5. ✅ **Proteção Zero Trust** (verificação contínua)

---

**Desenvolvido para A3 - Segurança em Computação e Sistemas**

