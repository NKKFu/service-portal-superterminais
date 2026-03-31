# Projeto Service Portal ST

Projeto desenvolvido com o propósito de avaliação por parte da empresa Super Terminais

> [!IMPORTANT]
> Os diagramas solicitados estão disponíveis em [docs/DIAGRAMS.md](docs/DIAGRAMS.md).

> [!IMPORTANT]
> As respostas da Avaliação Técnica - Parte 2 está disponível em [docs/CHALLENGE.md](docs/CHALLENGE.md#parte-02) no final do arquivo.

## Como Rodar e Usar

Requisitos:
- Docker
- Docker Compose

Para iniciar o projeto:
```bash
docker-compose up -d
```

### Links de Acesso:
- **Frontend (Portal)**: [http://localhost](http://localhost)
- **Backend (API + Swagger)**: [http://localhost/api/swagger-ui/index.html](http://localhost/api/swagger-ui/index.html)
- **PgAdmin (Banco de Dados)**: [http://localhost/pgadmin](http://localhost/pgadmin)
    - **Usuário**: `postgres@email.com`
    - **Senha**: `postgres` (padrão)

## Configuração Inicial

Com o sistema recém-instalado ele não possui usuários cadastrados. Para criar o **primeiro usuário interno (administrador)** e conseguir acessar o portal, siga o procedimento abaixo:

Execute o seguinte comando `curl` para registrar o primeiro usuário administrador utilizando o segredo de segurança:

```bash
curl -X 'POST' \
  'http://localhost/api/auth/register' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer secret-allow-first-user' \
  -d '{
  "username": "admin",
  "password": "senha_segura",
  "internal": true
}'
```

Após o sucesso da requisição, você poderá fazer login no [Frontend](http://localhost) com o usuário criado.

> [!TIP]
> O segredo `secret-allow-first-user` permite o registro de usuários sem a necessidade de estar logado.

## Arquitetura

A arquitetura do sistema é orquestrada via **Docker Compose**. O **Nginx** atua como um proxy reverso, centralizando o acesso e distribuindo as requisições para os serviços apropriados de acordo com a URI pesquisada.

```mermaid
graph TD
    User([Usuário]) --> Nginx[Nginx Reverse Proxy]

    subgraph "Service Portal ST"
        Nginx -- "/" --> Frontend[Frontend - Vue.js/Vite]
        Nginx -- "/api" --> API[Backend API - Spring Boot]
        Nginx -- "/pgadmin" --> PgAdmin[PgAdmin]

        API --> DB[(PostgreSQL DB)]
        PgAdmin --> DB
    end

    style Nginx fill:#009639,stroke:#333,stroke-width:2px
    style Frontend fill:#42b883,stroke:#333,stroke-width:2px,color:#fff
    style API fill:#6db33f,stroke:#333,stroke-width:2px,color:#fff
    style DB fill:#336791,stroke:#333,stroke-width:2px,color:#fff
    style User fill:#fff,stroke:#333,stroke-width:2px
```

### Componentes:
- **Nginx**: Ponto de entrada que gerencia o roteamento para portais e APIs.
- **Frontend**: SPA desenvolvida em Vue.js com Vite e TypeScript.
- **Backend**: API RESTful desenvolvida em Java com Spring Boot.
- **PostgreSQL**: Banco de dados relacional para persistência de dados e arquivos (bytea).
- **PgAdmin**: Interface web para administração do banco de dados.

## Decisões Técnicas

- Uso do Java, Spring Boot e VueJs
    - Desafio proposto pela empresa avaliadora

- Uso do Vite, Maven, TypeScript e Postgres
    - Familiaridade com as ferramentas

- Uso do Docker/Docker Compose e PgAdmin
    - Para fins de simplificação de desenvolvimento e gerenciamento de dependências

- Todo o projeto é acessível via Nginx
    - Para fins de simplificação de acesso e gerenciamento de portas

- Arquivos comprobatórios são armazenados no banco de dados Postgres
    - Por se tratar de um projeto de prova de conceito, não foi implementado um sistema de arquivos como o MinIO.

- Uso do algoritmo Argon2 ao invés do Bcrypt
    - O Bcrypt foi superado pelo Argon2

- Uso do Nginx como Reverse Proxy
    - Familiaridade com a ferramenta e centralização de acessos.

## Comandos Úteis (Docker Lifecycle)

### Ver logs
```bash
docker-compose logs -f
```

### Parar containers
```bash
docker-compose down
```

### Limpar dados do banco de dados (Reset Total)
```bash
docker-compose down -v
```

### Rebuild do projeto ao modificar código Java
```bash
docker-compose up --build -d api
```
