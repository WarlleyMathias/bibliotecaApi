# 📚 API Rest - Gestão de Biblioteca

API RESTful desenvolvida com Spring Boot 3 para gerenciamento de biblioteca, cobrindo operações completas de CRUD com validação de dados, regras de negócio e persistência em banco de dados PostgreSQL.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.4+
    * Spring Data JPA
    * Spring Web
    * Spring Validation
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker & Docker Compose
* **Testes:** JUnit 5, Mockito, MockMvc, ObjectMapper
* **Build Tool:** Maven

---

## 🏛️ Arquitetura do Projeto

```text
src/main/java/com/exemplo/biblioteca/
├── controller/     # Endpoints REST (HTTP Request/Response)
├── dto/            # Data Transfer Objects
├── model/          # Entidades do JPA mapeadas para o banco
├── repository/     # Interfaces do Spring Data JPA
├── service/        # Regras de negócio e validações
└── exception/      # Tratamento global de exceções