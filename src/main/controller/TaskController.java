package main.controller;


import main.model.CategoryTask;
import main.model.Task;
import main.model.enums.Priority;
import main.model.enums.StatusTask;
import main.service.CategoryService;
import main.service.TodoService;

import java.time.LocalDateTime;
import java.util.List;

public class TaskController {

    private final TodoService todoService;
    private final CategoryService categoryService;

    public TaskController(TodoService todoService, CategoryService categoryService) {
        this.todoService     = todoService;
        this.categoryService = categoryService;
    }

    public void create(String name, String description, LocalDateTime dateToEnd,
                       int priorityCode, Integer categoryId,
                       boolean alarmEnabled, int alarmHoursPrior) {
        Priority priority = Priority.fromCode(priorityCode);
        CategoryTask category = categoryId != null
            ? categoryService.findById(categoryId) : null;
        todoService.createTask(name, description, dateToEnd, priority,
                               category, alarmEnabled, alarmHoursPrior);
        System.out.println("✅ Tarefa criada com sucesso!");
    }

    public void update(Integer id, String name, String description,
                       LocalDateTime dateToEnd, Integer priorityCode,
                       Integer statusCode, Boolean alarmEnabled, Integer alarmHoursPrior) {
        Priority   priority = priorityCode != null ? Priority.fromCode(priorityCode)   : null;
        StatusTask status   = statusCode   != null ? StatusTask.fromCode(statusCode)   : null;
        todoService.updateTask(id, name, description, dateToEnd, priority,
                               status, alarmEnabled, alarmHoursPrior);
        System.out.println("✅ Tarefa atualizada!");
    }

    public void remove(Integer id) {
        todoService.removeTask(id);
        System.out.println("🗑️  Tarefa removida!");
    }

    public List<Task> listAll()                       { return todoService.listAll(); }
    public List<Task> listByStatus(StatusTask status) { return todoService.listByStatus(status); }
    public Task       findById(Integer id)            { return todoService.findById(id); }
    public void       checkAlarms()                   { todoService.checkAlarms(); }
}
