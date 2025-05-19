# Mobiauto Backend

API para gestão de revendas de veículos da Mobiauto.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- PostgreSQL
- JWT para autenticação
- Swagger para documentação
- Maven para gerenciamento de dependências
- Docker para containerização

## Pré-requisitos

- JDK 21
- Maven 3.9+
- PostgreSQL 15
- Criar banco mobiauto

## Configuração

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/mobiauto-backend-202502.git
   
2. Credenciais do banco: 
 - spring.datasource.username=
 - spring.datasource.password=
 
2.1 Ao iniciar o projeto automaticamente gerará as tabelas necessárias e alguns inserts de dados para teste.
   
## Swagger

- http://localhost:8080/swagger-ui/index.html
- Após autenticar, deve colar o token na header Authorization do swagger.
