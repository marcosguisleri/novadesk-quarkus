# 🎫 NovaDesk API (Quarkus)

API REST para gerenciamento de chamados (tickets) de um help desk interno, desenvolvida em Java 25 com **Quarkus**. É uma reescrita do [NovaDesk API](https://github.com/marcosguisleri/novadesk-api) original (feito em Spring Boot), usando a stack Quarkus com Panache e Jakarta REST.

## 📋 Sobre o projeto

O NovaDesk API permite criar, consultar, atualizar e remover tickets de suporte, com controle de **status** (`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CANCELED`) e **prioridade** (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`). Todo ticket criado recebe automaticamente o status inicial `OPEN` e a data/hora de criação.

## 🚀 Tecnologias

- **Java 25**
- **Quarkus 3.39.2**
- **Jakarta REST (RESTEasy Reactive)** — endpoints REST
- **Hibernate ORM com Panache** — persistência
- **Hibernate Validator** — validação de dados
- **PostgreSQL** (produção) / **H2** (testes)
- **SmallRye OpenAPI + Swagger UI** — documentação da API
- **JUnit + REST Assured** — testes automatizados
- **Docker / Docker Compose**
- **Maven** (com Maven Wrapper)

## ✨ Funcionalidades

- Criação de tickets com validação dos dados de entrada
- Listagem de tickets, com filtros opcionais por `status` e/ou `priority`
- Busca de ticket por ID
- Atualização de título, descrição, solicitante e status de um ticket existente
- Remoção de ticket
- Tratamento de erro dedicado para ticket não encontrado (`404`, via `ExceptionMapper`)
- Documentação interativa via Swagger UI

## 📌 Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tickets` | Cria um novo ticket |
| `GET` | `/tickets` | Lista todos os tickets (aceita `?status=` e/ou `?priority=`) |
| `GET` | `/tickets/{id}` | Busca um ticket pelo ID |
| `PUT` | `/tickets/{id}` | Atualiza um ticket existente |
| `DELETE` | `/tickets/{id}` | Remove um ticket |

### Modelo de Ticket

```json
{
  "id": 1,
  "title": "Impressora não funciona",
  "description": "A impressora do setor financeiro não está imprimindo.",
  "requester": "joao.silva",
  "status": "OPEN",
  "priority": "HIGH",
  "createdAt": "2026-09-07T14:30:00"
}
```

## ⚙️ Como executar

### Pré-requisitos

- Java 25 (apenas se for rodar sem Docker)
- Docker e Docker Compose

### Rodando com Docker Compose (recomendado)

1. Crie um arquivo `.env` na raiz do projeto com as variáveis:

   ```env
   POSTGRES_DB=novadesk
   POSTGRES_USER=novadesk
   POSTGRES_PASSWORD=sua_senha
   ```

2. Suba os containers:

   ```bash
   docker compose up --build
   ```

A API sobe em `http://localhost:8080` e o PostgreSQL em `localhost:5432`.

### Rodando em modo de desenvolvimento (dev mode)

Com um PostgreSQL disponível e as variáveis `POSTGRES_DB`, `POSTGRES_USER` e `POSTGRES_PASSWORD` exportadas no ambiente:

```bash
./mvnw quarkus:dev
```

O modo dev habilita live coding e disponibiliza a Dev UI em `http://localhost:8080/q/dev/`.

### Gerando o pacote executável

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### Executável nativo (GraalVM)

```bash
./mvnw package -Dnative
# ou, sem GraalVM instalado, buildando em container:
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## 📖 Documentação da API

Com a aplicação em execução, a documentação Swagger UI fica disponível em:

```
http://localhost:8080/q/swagger-ui
```

## 🧪 Testes

O projeto conta com testes de serviço e de resource (REST Assured). Para rodá-los:

```bash
./mvnw test
```

## 📁 Estrutura do projeto

```
src/main/java/br/dev/guisleri/novadesk
├── dto/              # DTOs de request/response
├── exception/        # Exceções customizadas
│   └── mapper/       # ExceptionMapper (JAX-RS) para tratamento de erros
├── model/            # Entidade Ticket e enums (status, priority)
├── repository/       # Repositório Panache
├── resource/         # Endpoints REST (JAX-RS)
└── service/          # Regras de negócio
```

## 👤 Autor

Desenvolvido por [Marcos Guisleri](https://github.com/marcosguisleri).
