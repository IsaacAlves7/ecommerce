# 🏪 POS System — Microservices com NFC-e e Email

> Java 17 · Spring Boot 3 · PostgreSQL · Flyway · Angular 17 · Docker · Apache Kafka · Elasticsearch · Webhook · REST API

Sistema completo de **Ponto de Venda (POS)** em arquitetura de microserviços, com emissão de **NFC-e (Nota Fiscal de Consumidor Eletrônica)** e envio automático por e-mail ao cliente.

---

## 🏗️ Arquitetura de Microserviços

```
┌──────────────┐     HTTP      ┌─────────────────┐
│  Angular 17  │ ─────────────► │   API Gateway   │ :8080
│   Frontend   │               │  Spring Cloud   │
└──────────────┘               └────────┬────────┘
                                        │ Route
              ┌─────────────────────────┼─────────────────────┐
              ▼                         ▼                     ▼
   ┌──────────────────┐     ┌──────────────────┐             ...
   │   POS Service    │     │  NFC-e Service   │
   │  :8081           │     │  :8082           │
   │  Spring Boot     │     │  Spring Boot     │
   │  PostgreSQL       │     │  PostgreSQL       │
   │  Flyway          │     │  Flyway + ZXing  │
   └────────┬─────────┘     └────────┬─────────┘
            │ KAFKA                  │ KAFKA
            │ pos.sale.confirmed     │ nfce.authorized
            ▼                        ▼
   ┌─────────────────────────────────────────┐
   │          Apache Kafka                   │
   │  Topics: pos.sale.confirmed             │
   │           pos.sale.cancelled            │
   │           nfce.authorized              │
   └─────────────────────────────────────────┘
                        │
                        ▼ nfce.authorized
            ┌───────────────────────┐
            │  Notification Service  │
            │  :8083                 │
            │  Spring Mail           │
            │  Thymeleaf Template    │
            │  SMTP (Gmail/etc)      │
            └───────────────────────┘
                        │
                        ▼
            📧 EMAIL COM DANFE NFC-e

   ┌─────────────────────────────────────────┐
   │  ELK Stack (Monitoramento)              │
   │  Elasticsearch :9200                    │
   │  Kibana        :5601                    │
   │  Logstash                               │
   └─────────────────────────────────────────┘
```

---

## 🔄 Fluxo Completo da Venda → NFC-e → Email

```
1. Operador adiciona itens no Angular POS Terminal
2. Informa email do cliente (opcional)
3. Seleciona forma de pagamento
4. Clica "Finalizar Venda"
         │
         ▼
5. POS Service cria Sale (status: PENDING)
6. POS Service confirma Sale (status: CONFIRMED)
7. POS Service publica evento Kafka: pos.sale.confirmed
         │
         ▼ (Kafka)
8. NFC-e Service consome pos.sale.confirmed
9. Gera XML NFC-e (modelo 65)
10. Simula autorização SEFAZ
11. Gera QR Code com ZXing
12. Gera DANFE HTML
13. Salva NFC-e (status: AUTHORIZED)
14. Publica evento Kafka: nfce.authorized
         │
         ▼ (Kafka)
15. Notification Service consome nfce.authorized
16. Renderiza template Thymeleaf com dados da NFC-e
17. Envia email HTML com QR Code + DANFE em anexo
         │
         ▼
18. 📧 Cliente recebe email com NFC-e!
```

---

## 🚀 Como Executar

### Pré-requisitos
- Docker & Docker Compose
- Java 17 (para build local)

### 1. Configurar variáveis de ambiente

```bash
cp .env.example .env
# Edite o .env com suas credenciais SMTP
```

### 2. Subir todos os serviços

```bash
docker-compose up --build -d
```

### 3. Verificar status

```bash
docker-compose ps
docker-compose logs -f pos-service
```

---

## 🌐 Serviços e URLs

| Serviço | URL | Descrição |
|---|---|---|
| Angular Frontend | http://localhost:4200 | Terminal POS |
| API Gateway | http://localhost:8080 | Entrada única |
| POS Service API | http://localhost:8081/swagger-ui.html | Swagger |
| NFC-e Service API | http://localhost:8082/swagger-ui.html | Swagger |
| Notification Service | http://localhost:8083 | Health |
| Kafka UI | http://localhost:8090 | Monitor Kafka |
| Kibana | http://localhost:5601 | Logs/Métricas |
| Elasticsearch | http://localhost:9200 | Search/Monitoring |

---

## 📡 API Reference

### POS Service

```bash
# Criar venda
POST /api/v1/sales
{
  "items": [
    { "productCode": "CAFE001", "productName": "Café", "unit": "UN",
      "quantity": 2, "unitPrice": 12.90, "taxRate": 12.0 }
  ],
  "paymentMethod": "PIX",
  "customerEmail": "cliente@email.com",
  "customerDocument": "12345678901",
  "customerName": "João Silva",
  "terminalId": "TERMINAL-001",
  "operatorId": "OPERATOR-001"
}

# Confirmar venda (dispara geração NFC-e via Kafka)
POST /api/v1/sales/{id}/confirm

# Buscar venda
GET /api/v1/sales/{id}
GET /api/v1/sales/code/{saleCode}

# Listar vendas
GET /api/v1/sales?page=0&size=20

# Cancelar
POST /api/v1/sales/{id}/cancel
```

### NFC-e Service

```bash
# Buscar NFC-e
GET /api/v1/nfce/{id}
GET /api/v1/nfce/sale/{saleCode}
GET /api/v1/nfce/key/{accessKey}

# Baixar DANFE
GET /api/v1/nfce/{id}/danfe

# Baixar XML
GET /api/v1/nfce/{id}/xml
```

---

## 🔧 Variáveis de Ambiente

| Variável | Descrição | Padrão |
|---|---|---|
| `SMTP_HOST` | Servidor SMTP | smtp.gmail.com |
| `SMTP_PORT` | Porta SMTP | 587 |
| `SMTP_USER` | Usuário SMTP | — |
| `SMTP_PASS` | Senha/App Password | — |
| `SMTP_FROM` | Email de envio | noreply@pos-system.com |
| `WEBHOOK_URL` | URL para webhook NFC-e | — |
| `WEBHOOK_SECRET` | Segredo do webhook | — |

### Gmail App Password
1. Ative 2FA na sua conta Google
2. Acesse `myaccount.google.com` → Segurança → Senhas de app
3. Gere senha para "E-mail"
4. Use como `SMTP_PASS`

---

## 📦 Stack Técnica

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem |
| Spring Boot | 3.2.0 | Framework base |
| Spring Data JPA | 3.x | Persistência |
| Spring Kafka | 3.x | Mensageria |
| Spring Cloud Gateway | 2023.0 | API Gateway |
| Spring Mail | 3.x | Email |
| Thymeleaf | 3.x | Templates HTML |
| PostgreSQL | 16 | Banco de dados |
| Flyway | 9.x | Migrations |
| Apache Kafka | 7.5.0 | Event Streaming |
| Elasticsearch | 8.11 | Monitoramento |
| Kibana | 8.11 | Dashboard |
| ZXing | 3.5.2 | Geração QR Code |
| Angular | 17 | Frontend |
| Docker | latest | Containers |
| Docker Compose | 3.8 | Orquestração |

---

## 🏛️ Padrões Arquiteturais

- ✅ **Arquitetura Hexagonal** (Ports & Adapters) em todos os serviços
- ✅ **Event-Driven Architecture** com Apache Kafka
- ✅ **Domain-Driven Design** (DDD) — domínios ricos
- ✅ **Database per Service** — cada microserviço tem seu PostgreSQL
- ✅ **API Gateway Pattern** — Spring Cloud Gateway
- ✅ **Webhook Integration** — disparo automático para sistemas externos
- ✅ **Observabilidade** — ELK Stack (Elasticsearch + Logstash + Kibana)
