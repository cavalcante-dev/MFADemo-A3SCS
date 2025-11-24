# 🔧 Configuração Backend para Vercel

Este documento explica como configurar o backend para funcionar com o frontend deployado no Vercel (`https://mfa-demo-a3-scs.vercel.app/`) enquanto o backend roda localmente.

## ✅ Configurações Realizadas

### 1. CORS Configurado
O backend foi configurado para aceitar requisições do domínio do Vercel:
- **Domínio Vercel:** `https://mfa-demo-a3-scs.vercel.app`
- **Localhost (desenvolvimento):** `http://localhost:5173` e `http://localhost:3000`

### 2. Arquivos Modificados
- `CorsConfig.java`: Configurado para permitir origens específicas
- `SecurityConfig.java`: Integrado com a configuração de CORS
- `application.properties`: Adicionada configuração de CORS

## 🚀 Como Funcionar com Backend Local

Para que o frontend no Vercel se comunique com o backend local, você precisa tornar o backend local acessível publicamente. Existem algumas opções:

### Opção 1: Usar ngrok (Recomendado para Testes)

1. **Instale o ngrok:**
   ```bash
   # Windows (via Chocolatey)
   choco install ngrok
   
   # Ou baixe de: https://ngrok.com/download
   ```

2. **Inicie o backend local:**
   ```bash
   cd mfademo-backend
   mvn spring-boot:run
   ```

3. **Em outro terminal, crie um túnel:**
   ```bash
   ngrok http 8080
   ```

4. **Copie a URL HTTPS fornecida pelo ngrok** (ex: `https://abc123.ngrok.io`)

5. **Configure no Vercel:**
   - Acesse o dashboard do Vercel
   - Vá em **Settings > Environment Variables**
   - Adicione/Atualize:
     - **Key:** `VITE_API_BASE_URL`
     - **Value:** `https://abc123.ngrok.io` (sua URL do ngrok)
     - **Environments:** Production, Preview, Development
   - Faça um novo deploy

6. **Atualize o CORS no backend** (se necessário):
   - Edite `application.properties` e adicione a URL do ngrok:
   ```properties
   cors.allowed-origins=https://mfa-demo-a3-scs.vercel.app,https://abc123.ngrok.io,http://localhost:5173,http://localhost:3000
   ```

### Opção 2: Usar localtunnel (Alternativa Gratuita)

1. **Instale o localtunnel:**
   ```bash
   npm install -g localtunnel
   ```

2. **Inicie o backend local:**
   ```bash
   cd mfademo-backend
   mvn spring-boot:run
   ```

3. **Crie um túnel:**
   ```bash
   lt --port 8080
   ```

4. **Siga os passos 5 e 6 da Opção 1** usando a URL fornecida pelo localtunnel

### Opção 3: Deploy do Backend (Produção)

Para produção, é recomendado fazer deploy do backend também. Opções populares:
- **Railway:** https://railway.app
- **Render:** https://render.com
- **Heroku:** https://heroku.com
- **AWS/GCP/Azure:** Para soluções enterprise

## 📝 Configuração Atual do CORS

O backend está configurado para aceitar requisições de:
- `https://mfa-demo-a3-scs.vercel.app` (Frontend no Vercel)
- `http://localhost:5173` (Frontend local - Vite)
- `http://localhost:3000` (Frontend local - React padrão)

### Adicionar Novas Origens

Para adicionar novas origens (como URL do túnel), edite `application.properties`:

```properties
cors.allowed-origins=https://mfa-demo-a3-scs.vercel.app,https://sua-url-tunel.com,http://localhost:5173,http://localhost:3000
```

Ou configure via variável de ambiente:
```bash
export CORS_ALLOWED_ORIGINS="https://mfa-demo-a3-scs.vercel.app,https://sua-url-tunel.com"
```

## 🔍 Verificação

Após configurar, verifique:

1. **Backend está rodando:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **CORS está funcionando:**
   - Abra o console do navegador no frontend do Vercel
   - Tente fazer login
   - Verifique se não há erros de CORS

3. **Logs do backend:**
   - Verifique os logs do Spring Boot para ver as requisições chegando

## ⚠️ Importante

- **Segurança:** Túneis como ngrok expõem seu backend local publicamente. Use apenas para desenvolvimento/testes.
- **URLs Dinâmicas:** URLs de túneis gratuitos mudam a cada reinicialização. Considere planos pagos para URLs fixas.
- **Produção:** Para produção, sempre faça deploy do backend em um serviço adequado.

## 🐛 Troubleshooting

### Erro "Invalid character found in method name" / "HTTP method names must be tokens"

**Sintoma:** Logs mostram erro sobre parsing HTTP com caracteres inválidos.

**Causa:** O servidor HTTP está recebendo requisições HTTPS (handshake TLS). Isso acontece quando:
- Alguém tenta acessar `https://localhost:8080` diretamente no navegador
- O frontend HTTPS tenta acessar o backend HTTP local (sem túnel)

**Solução:** 
- Este erro é apenas um warning e não quebra a aplicação
- Os logs foram configurados para suprimir esse warning (ver `application.properties`)
- Para produção, use um túnel HTTPS (ngrok) ou faça deploy do backend com HTTPS

### Erro de CORS
- Verifique se a URL do frontend está na lista de origens permitidas
- Verifique se o backend está usando `corsConfigurationSource` no `SecurityConfig`
- Limpe o cache do navegador

### Backend não acessível
- Verifique se o firewall permite conexões na porta 8080
- Verifique se o túnel está ativo e funcionando
- Teste a URL do túnel diretamente no navegador

### Variáveis de ambiente não funcionam
- Certifique-se de que as variáveis começam com `VITE_` no frontend
- Faça um novo deploy após adicionar variáveis
- Verifique se as variáveis estão configuradas para o ambiente correto

