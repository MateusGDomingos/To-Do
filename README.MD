# TODO API

API REST de lista de tarefas (To-Do), em construção, para cadastrar, consultar e concluir tarefas.

Ainda estou montando o system design e implementando. As classes já apontam o domínio e as camadas, mas a maior parte ainda é rascunho (pacotes, anotações Spring/JPA, validações e endpoints ainda não estão fechados).

## O que a API deve fazer

Cada **tarefa** representa um item da lista, com:

| Campo | Papel |
| --- | --- |
| `id` | Identificador |
| `titulo` | Nome curto da tarefa |
| `descricao` | Detalhe opcional |
| `conclusao` | Se já foi feita (`false` por padrão) |
| `prioridade` | `ALTA`, `MEDIA` ou `BAIXA` |
| `dataCriacao` | Preenchida na criação |
| `dataConclusao` | `null` até a tarefa ser marcada como concluída |

O recurso HTTP previsto é `/tarefas`. Ainda não coloquei o  CRUD no controller.

## Como está organizado

```
ROTA COMPLETA/
├── docker-compose.yml          # PostgreSQL 15 (tododb)
└── todoapi/                    # Spring Boot (Maven)
    └── src/main/com/pratica/todoapi/
        ├── TodoapiApplication.java
        ├── Controller/         # Tarefacontroller → /tarefas
        ├── Model/              # Tarefa, Prioridade
        ├── DTO/                # entrada (request) e saída (response)
        ├── Repository/         # persistência via Spring Data JPA
        └── Config/             # OpenAPI / Swagger (esqueleto)
```

Fluxo pretendido:

1. Cliente envia JSON no formato de `TarefaRequestDTO` (título, descrição, prioridade).
2. Controller recebe em `/tarefas`.
3. Repository grava/consulta no PostgreSQL.
4. API devolve `TarefaResponseDTO` (id, datas, status de conclusão, etc.).

Há uma classe `Tarefas` rascunhada também em `TodoapiApplication` — o modelo canônico deve ficar em `Model/Tarefa`.

## Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web, Data JPA, Validation
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Docker Compose para o banco

O `pom.xml` ainda mistura dependências dentro e fora de `<dependencies>` (web, JPA, PostgreSQL, validation, OpenAPI e Lombok precisam ficar no bloco certo para o Maven resolver).

## Banco de dados

`docker-compose.yml` sobe o Postgres:

- usuário: `admin`
- senha: `admin123`
- banco: `tododb`
- porta: `5432`

A intenção em `application.yml` é JPA com `ddl-auto: update` e JDBC em `jdbc:postgresql://db:5432/tododb`. O YAML atual mistura `server` e `spring` (porta da API vs dados do banco); o app Spring, rodando na máquina, em geral usa `localhost:5432`, enquanto `db` é o hostname **dentro** da rede do Compose.

## Como subir o banco

Na raiz do repositório:

```bash
docker compose up -d
```

A aplicação (`todoapi`) ainda não está empacotada/rodando de ponta a ponta neste estado do código.

## Documentação da API

Há um `OpenApiConfig` preparado para Swagger. Quando a app subir com Springdoc, o esperado é algo como:

- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Estado atual (honesto)

Já existe a ideia do produto e os arquivos das camadas. Ainda falta, entre outras coisas:

- endpoints REST (criar, listar, buscar, atualizar, concluir, apagar)
- entidade JPA + enum de prioridade ligado à tarefa
- `TarefaRepository` estendendo `JpaRepository`
- DTOs com validação (`titulo` obrigatório, tamanho razoável, prioridade obrigatória)
- `pom.xml` e `application.yml` consistentes
- controller só com a API (hoje há código solto de outro exercício no mesmo arquivo)

Este README descreve o **entendimento do projeto**, não um sistema já operacional.
