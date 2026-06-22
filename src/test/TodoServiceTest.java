package test;

import main.model.CategoryTask;
import main.model.Task;
import main.model.enums.Priority;
import main.model.enums.StatusTask;
import main.observer.TaskObserver;
import main.repository.CategoryRepository;
import main.repository.TaskRepository;
import main.service.TodoService;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TodoService — Testes unitários")
class TodoServiceTest {

    private TodoService todoService;
    private CategoryTask categoria;

    // Observer de teste para capturar eventos
    private final List<String> eventos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        CategoryRepository catRepo  = new CategoryRepository();
        TaskRepository taskRepo = new TaskRepository(catRepo);
        todoService = new TodoService(taskRepo);
        todoService.addObserver(new TaskObserver() {
            public void onTaskCreated(Task t) { eventos.add("CREATED:" + t.getName()); }
            public void onTaskUpdated(Task t) { eventos.add("UPDATED:" + t.getName()); }
            public void onTaskRemoved(Integer id) { eventos.add("REMOVED:" + id); }
        });
        categoria = CategoryTask.create("Dev", "Desenvolvimento");
        eventos.clear();
    }

    @Test
    @DisplayName("Criar tarefa adiciona à lista e dispara observer")
    void deveCriarTarefaENotificarObserver() {
        todoService.createTask("Estudar Java", "Revisar streams",
            LocalDateTime.now().plusDays(1), Priority.HIGH, categoria, false, 0);

        List<Task> tasks = todoService.listAll();
        assertEquals(1, tasks.size());
        assertEquals("Estudar Java", tasks.get(0).getName());
        assertTrue(eventos.stream().anyMatch(e -> e.startsWith("CREATED:")));
    }

    @Test
    @DisplayName("Remover tarefa existente funciona e dispara observer")
    void deveRemoverTarefaENotificarObserver() {
        todoService.createTask("Tarefa temp", "desc",
            LocalDateTime.now().plusDays(1), Priority.LOW, null, false, 0);
        Integer id = todoService.listAll().get(0).getId();

        todoService.removeTask(id);

        assertTrue(todoService.listAll().isEmpty());
        assertTrue(eventos.stream().anyMatch(e -> e.startsWith("REMOVED:")));
    }

    @Test
    @DisplayName("Remover ID inexistente lança exceção")
    void develancarExcecaoAoRemoverIdInexistente() {
        assertThrows(IllegalArgumentException.class, () -> todoService.removeTask(9999));
    }

    @Test
    @DisplayName("Atualizar status da tarefa funciona")
    void deveAtualizarStatusDaTarefa() {
        todoService.createTask("Tarefa X", "desc",
            LocalDateTime.now().plusDays(1), Priority.MEDIUM, null, false, 0);
        Integer id = todoService.listAll().get(0).getId();

        todoService.updateTask(id, null, null, null, null, StatusTask.DONE, null, null);

        assertEquals(StatusTask.DONE, todoService.findById(id).getStatus());
        assertTrue(eventos.stream().anyMatch(e -> e.startsWith("UPDATED:")));
    }

    @Test
    @DisplayName("Listar por status retorna apenas tarefas corretas")
    void deveListarTarefasPorStatus() {
        todoService.createTask("T1", "d", LocalDateTime.now().plusDays(1), Priority.LOW,  null, false, 0);
        todoService.createTask("T2", "d", LocalDateTime.now().plusDays(1), Priority.HIGH, null, false, 0);

        Integer id1 = todoService.listAll().get(0).getId();
        todoService.updateTask(id1, null, null, null, null, StatusTask.DONE, null, null);

        List<Task> done = todoService.listByStatus(StatusTask.DONE);
        assertEquals(1, done.size());
        assertEquals("T1", done.get(0).getName());
    }

    @Test
    @DisplayName("Tarefas são ordenadas por prioridade (maior primeiro)")
    void deveOrdenarPorPrioridade() {
        todoService.createTask("Baixa",  "d", LocalDateTime.now().plusDays(1), Priority.LOW,    null, false, 0);
        todoService.createTask("Urgente","d", LocalDateTime.now().plusDays(1), Priority.URGENT, null, false, 0);
        todoService.createTask("Média",  "d", LocalDateTime.now().plusDays(1), Priority.MEDIUM, null, false, 0);

        List<Task> sorted = todoService.listAll();
        assertEquals("Urgente", sorted.get(0).getName());
        assertEquals("Média",   sorted.get(1).getName());
        assertEquals("Baixa",   sorted.get(2).getName());
    }
}
