# Projeto Service Portal ST

Projeto desenvolvido com o propósito de avaliação por parte da empresa Super Terminais

Os diagramas solicitados estão disponíveis em [docs/DIAGRAMS.md](docs/DIAGRAMS.md).

> [!IMPORTANT]
> As respostas da Avaliação Técnica - Parte 2 está disponível em [docs/CHALLENGE.md](docs/CHALLENGE.md#parte-02) no final do arquivo.

## Como Rodar e Usar

Requisitos:
- Docker
- Docker Compose

Para iniciar o projeto, use o seguinte comando:
```bash
docker-compose up -d
```

Ao fazer alguma modificação, use o seguinte comando para reiniciar:

```bash
docker-compose run --rm api mvn clean compile
docker-compose restart api
docker-compose up -d
```

Após a finalização do Deployment, as seguintes partes do sistema estarão disponíveis:
- Backend: http://localhost/api/swagger-ui/index.html
- Frontend: http://localhost
- PgAdmin: http://localhost/pgadmin
    - Usuário (padrão): postgres@email.com
    - Senha (padrão): postgres

> [!WARNING]
> Para cadastrar usuários no sistema sem a necessidade de estar logado, basta passar o parâmetro `secret-allow-first-user` na header do request de cadastro.
> Exemplo:
> ```curl
> curl -X 'POST' \
>   'http://localhost/api/auth/register' \
>   -H 'accept: */*' \
>   -H 'Authorization: Bearer secret-allow-first-user' \
>   -H 'Content-Type: application/json' \
>   -d '{
>   "username": "teste",
>   "password": "senha",
>   "internal": true
> }'
> ```

## Arquitetura

> Em desenvolvimento..

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
