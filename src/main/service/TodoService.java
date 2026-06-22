package main.service;


import main.factory.TaskFactory;
import main.model.CategoryTask;
import main.model.Task;
import main.model.enums.Priority;
import main.model.enums.StatusTask;
import main.observer.TaskObserver;
import main.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoService {

    private final TaskRepository repository;
    private final List<TaskObserver> observers = new ArrayList<>();

    public TodoService(TaskRepository repository) {
        this.repository = repository;
    }

    // ── Observer Pattern ──────────────────────────────────────────────────────

    public void addObserver(TaskObserver observer)    { observers.add(observer); }
    public void removeObserver(TaskObserver observer) { observers.remove(observer); }

    private void notifyCreated(Task task) { observers.forEach(o -> o.onTaskCreated(task)); }
    private void notifyUpdated(Task task) { observers.forEach(o -> o.onTaskUpdated(task)); }
    private void notifyRemoved(Integer id){ observers.forEach(o -> o.onTaskRemoved(id)); }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public void createTask(String name, String description, LocalDateTime dateToEnd,
                           Priority priority, CategoryTask category,
                           boolean alarmEnabled, int alarmHoursPrior) {
        Task task = TaskFactory.createFull(name, description, dateToEnd,
                                           priority, category, alarmEnabled, alarmHoursPrior);
        repository.save(task);
        notifyCreated(task);
    }

    public void updateTask(Integer id, String name, String description,
                           LocalDateTime dateToEnd, Priority priority,
                           StatusTask status, Boolean alarmEnabled, Integer alarmHoursPrior) {
        Task task = findById(id);
        task.updateData(name, description, dateToEnd, priority, status, alarmEnabled, alarmHoursPrior);
        repository.update(task);
        notifyUpdated(task);
    }

    public void removeTask(Integer id) {
        findById(id); // valida existência
        repository.delete(id);
        notifyRemoved(id);
    }

    public Task findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada: " + id));
    }

    // ── Listagens ─────────────────────────────────────────────────────────────

    public List<Task> listAll() {
        List<Task> sorted = new ArrayList<>(repository.findAll());
        Collections.sort(sorted);
        return sorted;
    }

    public List<Task> listByStatus(StatusTask status) {
        return repository.findAll().stream()
            .filter(t -> t.getStatus() == status)
            .sorted()
            .toList();
    }

    public List<Task> listByPriorityNumber(Integer number) {
        return repository.findAll().stream()
            .filter(t -> t.getPriority().getNumber().equals(number))
            .toList();
    }

    public void checkAlarms() {
        repository.findAll().stream()
            .filter(Task::shouldTriggerAlarm)
            .forEach(t -> System.out.println("⏰ ALARME: " + t.getName()));
    }

    public void load(CategoryService categoryService) {
        repository.loadAll();
    }
}
