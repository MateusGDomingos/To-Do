# ROTA COMPLETA — Todo API (análise do estado atual)

> Análise feita em 29/09/2026 sobre tudo que existe no diretório até agora.

## 1. Visão geral

O objetivo do projeto é uma **API REST de tarefas (To-Do)** com:

- **Spring Boot 4.1.1** + **Java 25** (gerado pelo Spring Initializr, build com Maven Wrapper)
- **PostgreSQL 15** rodando via **Docker Compose**
- **Spring Data JPA** para persistência
- **Bean Validation** para validar entrada
- **springdoc-openapi** (Swagger UI) para documentação
- **Lombok** para reduzir boilerplate

**Status atual: esqueleto inicial — o projeto ainda não compila.** A infraestrutura (Docker, Maven) está encaminhada, mas as classes Java estão incompletas e há erros de sintaxe e de configuração (detalhados na seção 4).

## 2. Estrutura de arquivos

```
ROTA COMPLETA/
├── README.MD                  ← este arquivo
├── docker-compose.yml         ← banco PostgreSQL
├── .idea/                     ← configuração do IntelliJ
└── todoapi/
    ├── pom.xml                ← dependências Maven
    ├── mvnw / mvnw.cmd        ← Maven Wrapper
    └── src/
        ├── main/
        │   ├── java/com/pratica/todoapi/
        │   │   ├── TodoapiApplication.java     ← classe principal (+ classe Tarefas dentro)
        │   │   ├── Config/OpenApiConfig.java   ← config do Swagger (vazia)
        │   │   └── Controller/Tarefacontroller.java ← controller (vazio) + classe Venda
        │   └── resources/application.properties
        └── test/java/com/pratica/todoapi/TodoapiApplicationTests.java
```

## 3. O que já foi construído

| Componente | Situação | Comentário |
|---|---|---|
| `docker-compose.yml` | ✅ Funcional | Sobe `postgres:15` na porta 5432, banco `tododb`, usuário `admin`, volume persistente `postgres_data`. |
| Projeto Maven (Initializr) | ⚠️ Parcial | Estrutura correta, mas o `pom.xml` está malformado (ver 4.1). |
| `TodoapiApplication` | ⚠️ Parcial | `main` correto, porém contém uma classe `Tarefas` aninhada com erro de sintaxe. |
| Modelo `Tarefas` | 🟡 Esboço | Campos definidos: `id`, `titulo`, `descricao`, `concluido`, `prioridade`, `dataCriacao`, `dataConclusao`. Ainda não é entidade JPA. |
| `Tarefacontroller` | 🟡 Esboço | Anotado com `@RestController` e `@RequestMapping("/tarefas")`, sem endpoints. |
| `OpenApiConfig` | 🟡 Esboço | Apenas a declaração da classe e um `@Bean` solto. |
| `application.properties` | 🟡 Mínimo | Só tem `spring.application.name`. Falta a conexão com o banco. |
| Teste `contextLoads` | ⚪ Padrão | Teste gerado pelo Initializr; vai falhar enquanto o projeto não compilar/conectar ao banco. |

## 4. Problemas encontrados

### 4.1 `pom.xml`

1. **Dependências fora do bloco `<dependencies>`** — `web`, `data-jpa`, `postgresql`, `validation`, `springdoc` e `lombok` foram coladas depois de `</build>`, direto dentro de `<project>`. O Maven rejeita isso. Elas precisam ir dentro de `<dependencies>`.
2. **`springdoc-openapi-starter-webmvc-ui` sem versão** — essa biblioteca não é gerenciada pelo Spring Boot, então a versão é obrigatória. Para Spring Boot 4 é preciso usar a linha **3.x** do springdoc.
3. **Lombok 1.18.30 fixado** — é antigo demais para Java 25. Remova o `<version>` (o Spring Boot já gerencia uma versão compatível) e troque o escopo para `<optional>true</optional>`, como o Initializr faz.
4. No Spring Boot 4, `spring-boot-starter-web` foi renomeado para `spring-boot-starter-webmvc` (o nome antigo ainda funciona, mas está deprecado).

### 4.2 `TodoapiApplication.java`

- A classe `Tarefas` está **dentro** da classe principal e começa com `Public` (P maiúsculo) → erro de compilação.
- `LocalDateTime` é usado sem `import java.time.LocalDateTime;`.
- `private enum prioridade {...}` declara só o **tipo** do enum, não um campo. Falta algo como `private Prioridade prioridade;`.
- Campos `public` quebram o encapsulamento; o ideal são campos `private` com getters/setters (ou Lombok `@Getter/@Setter`).
- **Sugestão:** mover para `model/Tarefa.java` como `@Entity`, com `@Id @GeneratedValue private Long id;` e `@Enumerated(EnumType.STRING)` na prioridade.

### 4.3 `Config/OpenApiConfig.java`

- Sem `package com.pratica.todoapi.Config;` e sem imports.
- Falta `@Configuration` na classe.
- `@Bean` está solto, sem método — erro de compilação. Deve anotar um método que retorna `OpenAPI` (título, versão, descrição da API).

### 4.4 `Controller/Tarefacontroller.java`

- Sem declaração de `package`.
- `@RequestMapping` usado sem import.
- `import java.ArrayList;` está errado → o correto é `java.util.ArrayList`.
- **A classe `Venda` (exercício de Scanner/preço) está no mesmo arquivo** — um arquivo Java só pode ter uma classe `public`, e ela não tem relação com a API. Deve ser removida ou movida para outro projeto de exercícios.
- Convenção de nomes: `TarefaController` (C maiúsculo). Pacotes em Java costumam ser minúsculos (`config`, `controller`).

### 4.5 `application.properties`

Com `spring-boot-starter-data-jpa` no classpath, a aplicação **não sobe** sem datasource. Falta algo como:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tododb
spring.datasource.username=admin
spring.datasource.password=admin123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4.6 Outros pontos

- **Não é um repositório Git** — vale rodar `git init` para versionar o progresso.
- Senha do banco em texto puro no `docker-compose.yml`: aceitável para estudo local; em algo real, usar arquivo `.env` (que já está no `.gitignore`).
- Nenhuma lógica de negócio, repositório ou tratamento de erro existe ainda.

## 5. Arquitetura sugerida (próximo alvo)

```
com.pratica.todoapi
├── TodoapiApplication.java
├── config/OpenApiConfig.java
├── controller/TarefaController.java      ← endpoints REST
├── service/TarefaService.java            ← regras de negócio
├── repository/TarefaRepository.java      ← extends JpaRepository<Tarefa, Long>
├── model/Tarefa.java, Prioridade.java    ← entidade + enum
├── dto/TarefaRequest.java, TarefaResponse.java  ← com @NotBlank, @Size...
└── exception/GlobalExceptionHandler.java ← @RestControllerAdvice (404, 400)
```

Endpoints previstos:

| Método | Rota | Ação |
|---|---|---|
| `GET` | `/tarefas` | Listar tarefas |
| `GET` | `/tarefas/{id}` | Buscar por id |
| `POST` | `/tarefas` | Criar tarefa |
| `PUT` | `/tarefas/{id}` | Atualizar tarefa |
| `PATCH` | `/tarefas/{id}/concluir` | Marcar como concluída (preenche `dataConclusao`) |
| `DELETE` | `/tarefas/{id}` | Remover tarefa |

## 6. Roteiro de próximos passos

1. [ ] Corrigir o `pom.xml` (dependências dentro de `<dependencies>`, versão do springdoc, Lombok).
2. [ ] Remover a classe `Venda` do controller.
3. [ ] Tirar `Tarefas` de dentro da classe principal e criar a entidade `Tarefa` + enum `Prioridade`.
4. [ ] Adicionar `package` e imports corretos em todos os arquivos.
5. [ ] Configurar o datasource no `application.properties`.
6. [ ] Criar `TarefaRepository` e `TarefaService`.
7. [ ] Implementar os endpoints CRUD no controller.
8. [ ] Adicionar DTOs com validação e o `GlobalExceptionHandler`.
9. [ ] Completar o `OpenApiConfig` e testar em `http://localhost:8080/swagger-ui.html`.
10. [ ] Escrever testes (service com Mockito, controller com MockMvc).
11. [ ] `git init` e primeiro commit.

## 7. Como rodar (depois das correções)

```bash
# 1. Subir o banco
docker compose up -d

# 2. Rodar a API
cd todoapi
./mvnw spring-boot:run

# 3. Documentação
# http://localhost:8080/swagger-ui.html
```
