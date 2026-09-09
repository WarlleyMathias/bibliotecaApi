# 📚 Biblioteca API

> API RESTful robusta para gerenciamento completo de acervo bibliográfico, empréstimos e usuários.

---

## 🛠️ Tecnologias e Ferramentas

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3
- **Persistência & Banco de Dados:** Spring Data JPA, PostgreSQL, Flyway / Hibernate
- **Validação & Exceções:** Bean Validation, Global Exception Handling (`@ControllerAdvice`)
- **Testes:** JUnit 5, Mockito
- **Documentação:** OpenAPI / Swagger UI
- **Containerização:** Docker & Docker Compose
- **Build Tool:** Maven

---

## 📌 Funcionalidades Principais

- [x] **Gestão de Livros & Autores:** Cadastro, atualização, listagem e remoção com validações de dados.
- [x] **Controle de Empréstimos:** Regras de negócio para verificação de disponibilidade, prazos e devoluções.
- [x] **Tratamento de Erros:** Respostas HTTP padronizadas (RFC 7807 / Problem Details) para falhas de validação e recursos não encontrados.
- [x] **Testes Automatizados:** Cobertura de testes unitários para serviços e regras de negócio com Mockito.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 17+
- Maven
- Docker / Docker Compose (opcional)

### Passos

# 1. Clone o repositório
git clone https://github.com/WarlleyMathias/NOME-DO-SEU-REPOSISTORIO.git

# 2. Suba o banco de dados com Docker
docker-compose up -d

# 3. Execute a aplicação
mvn spring-boot:run

# 4. Acesse a documentação no navegador:
http://localhost:8080/swagger-ui.html
