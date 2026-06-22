# TODO List — ACZG Refatorado

**Autor: Saulo**

Refatoração completa do projeto TODO List aplicando **MVC**, **SOLID** e **Design Patterns**.

---

## Estrutura MVC

```
src/main/java/br/com/aczg/todolist/
├── model/                  ← MODEL: dados puros (Task, CategoryTask, enums)
│   ├── enums/
│   │   ├── Priority.java
│   │   └── StatusTask.java
│   ├── Task.java
│   └── CategoryTask.java
├── repository/             ← MODEL: persistência (CSV)
│   ├── Repository.java     ← interface genérica
│   ├── TaskRepository.java
│   └── CategoryRepository.java
├── service/                ← MODEL: regras de negócio
│   ├── TodoService.java
│   └── CategoryService.java
├── factory/                ← MODEL: criação de objetos
│   ├── TaskFactory.java
│   └── CategoryFactory.java
├── observer/               ← MODEL: eventos assíncronos
│   ├── TaskObserver.java
│   └── AlarmObserver.java
├── controller/             ← CONTROLLER: ponte View ↔ Service
│   ├── TaskController.java
│   └── CategoryController.java
├── view/                   ← VIEW: toda a UI (menus, input/output)
│   └── MenuHandler.java
└── Main.java               ← Composição Root + loop principal
```

---

## Design Patterns aplicados

### 1. Factory Method (`factory/`)
**Problema original:** `Task.createTask(...)` era chamado espalhado pelo código com muitos parâmetros difíceis de lembrar.

**Solução:** `TaskFactory` e `CategoryFactory` centralizam a criação de objetos. Se surgir um novo campo obrigatório, só o factory muda (SOLID-O).

```java
// Antes: espalhado, acoplado
Task t = Task.createTask(name, desc, date, Priority.MEDIUM, cat, false, 0);

// Depois: semântico e centralizado
Task t = TaskFactory.createSimple(name, desc, date, cat);
Task urgente = TaskFactory.createUrgent(name, desc, date, cat);
```

### 2. Observer (`observer/`)
**Problema original:** `checkAlarms()` ficava dentro do `TodoManager` — misturava lógica de negócio com comportamento de notificação (violava SOLID-S).

**Solução:** `TaskObserver` define o contrato. Qualquer comportamento ao criar/atualizar/remover tarefas (alarme, log, email) é implementado separadamente e registrado no service. O service não precisa ser alterado para novos comportamentos (SOLID-O).

```java
todoService.addObserver(new AlarmObserver());   // alarme
todoService.addObserver(new LogObserver());     // log futuro - sem alterar o service
```

### 3. Repository (`repository/`)
**Problema original:** lógica de CSV espalhada dentro dos services (`saveCategoriesCSV`, `loadTasksCSV`), acoplando persistência com negócio.

**Solução:** `Repository<T, ID>` é uma interface genérica. `TaskRepository` e `CategoryRepository` implementam a persistência em CSV. Trocar para banco de dados = criar `TaskJpaRepository` sem tocar em nenhum service.

### 4. MVC (arquitetura)
**Problema original:** `MenuHandler` misturava leitura de input, lógica de negócio e chamadas de persistência.

**Solução:**
- **Model:** `Task`, `CategoryTask`, enums, repositories, services
- **Controller:** `TaskController`, `CategoryController` — ponte entre View e Service
- **View:** `MenuHandler` — só lê input e exibe output, chama Controllers

---

## Princípios SOLID aplicados

| Princípio | Onde | Como |
|-----------|------|------|
| **S** — Responsabilidade Única | Todas as classes | `Task` só tem dados; `TodoService` só orquestra; `MenuHandler` só faz UI; `Repository` só persiste |
| **O** — Aberto/Fechado | Observer + Repository | Novos comportamentos (log, email) via novo Observer sem alterar o service; nova persistência via novo Repository |
| **L** — Substituição de Liskov | `Repository<T,ID>` | `TaskRepository` substitui a interface sem quebrar comportamento |
| **I** — Segregação de Interface | `Repository`, `TaskObserver` | Interfaces pequenas e focadas (não forçam implementar o que não precisam) |
| **D** — Inversão de Dependência | Todos os construtores | `TodoService` recebe `TaskRepository` (injetado); `TaskController` recebe `TodoService` (injetado); nada instancia suas dependências internamente |

---

## Como executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Compilar e testar
```bash
mvn clean test
```

### Rodar a aplicação
```bash
mvn clean package
java -jar target/todo-list-2.0.0.jar
```

---

## Tecnologias
- Java 17
- Maven
- JUnit 5 (testes unitários)
- Persistência em CSV (sem frameworks)
