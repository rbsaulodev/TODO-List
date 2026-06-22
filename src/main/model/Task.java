package main.model;


import main.model.enums.Priority;
import main.model.enums.StatusTask;

import java.time.LocalDateTime;
import java.util.Objects;


public class Task implements Comparable<Task> {

    private static int idCounter = 1;

    private final Integer       id;
    private String              name;
    private String              description;
    private LocalDateTime       dateToEnd;
    private Priority            priority;
    private CategoryTask        category;       // ← injeção de dependência (SOLID-D)
    private StatusTask status;
    private boolean             alarmEnabled;
    private int                 alarmHoursPrior;

    /** Construtor privado — use o factory method */
    private Task(String name, String description, LocalDateTime dateToEnd,
                 Priority priority, CategoryTask category, StatusTask status,
                 boolean alarmEnabled, int alarmHoursPrior) {
        this.id              = idCounter++;
        this.name            = Objects.requireNonNull(name, "Nome não pode ser nulo");
        this.description     = description;
        this.dateToEnd       = dateToEnd;
        this.priority        = priority;
        this.category        = category;
        this.status          = StatusTask.TODO;
        this.alarmEnabled    = alarmEnabled;
        this.alarmHoursPrior = alarmHoursPrior;
    }

    /** Construtor de reconstituição (carga do CSV) */
    public Task(Integer id, String name, String description, LocalDateTime dateToEnd,
                Priority priority, CategoryTask category, StatusTask status,
                boolean alarmEnabled, int alarmHoursPrior) {
        this.id              = id;
        this.name            = name;
        this.description     = description;
        this.dateToEnd       = dateToEnd;
        this.priority        = priority;
        this.category        = category;
        this.status          = status;
        this.alarmEnabled    = alarmEnabled;
        this.alarmHoursPrior = alarmHoursPrior;
        if (id >= idCounter) idCounter = id + 1;
    }

    /** Factory Method */
    public static Task createTask(String name, String description, LocalDateTime dateToEnd,
                                  Priority priority, CategoryTask category,
                                  boolean alarmEnabled, int alarmHoursPrior) {
        return new Task(name, description, dateToEnd, priority, category,
                        StatusTask.TODO, alarmEnabled, alarmHoursPrior);
    }

    public void updateData(String name, String description, LocalDateTime dateToEnd,
                           Priority priority, StatusTask status,
                           Boolean alarmEnabled, Integer alarmHoursPrior) {
        if (name            != null && !name.isBlank())        this.name            = name;
        if (description     != null && !description.isBlank()) this.description     = description;
        if (dateToEnd       != null)                           this.dateToEnd       = dateToEnd;
        if (priority        != null)                           this.priority        = priority;
        if (status          != null)                           this.status          = status;
        if (alarmEnabled    != null)                           this.alarmEnabled    = alarmEnabled;
        if (alarmHoursPrior != null)                           this.alarmHoursPrior = alarmHoursPrior;
    }

    public boolean shouldTriggerAlarm() {
        if (!alarmEnabled || dateToEnd == null) return false;
        LocalDateTime alarmTime = dateToEnd.minusHours(alarmHoursPrior);
        LocalDateTime now       = LocalDateTime.now();
        return now.isAfter(alarmTime) && now.isBefore(dateToEnd);
    }

    /** Ordena por prioridade (maior primeiro) */
    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority.getNumber(), this.priority.getNumber());
    }

    public String toCSV() {
        return id + "," + name + "," + description + "," + dateToEnd + ","
               + priority.getNumber() + "," + (category != null ? category.getId() : "")
               + "," + status.getNumber() + "," + alarmEnabled + "," + alarmHoursPrior;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Integer       getId()              { return id; }
    public String        getName()            { return name; }
    public String        getDescription()     { return description; }
    public LocalDateTime getDateToEnd()       { return dateToEnd; }
    public Priority      getPriority()        { return priority; }
    public CategoryTask  getCategory()        { return category; }
    public StatusTask    getStatus()          { return status; }
    public boolean       isAlarmEnabled()     { return alarmEnabled; }
    public int           getAlarmHoursPrior() { return alarmHoursPrior; }

    public void setName(String name)                      { this.name            = name; }
    public void setDescription(String description)        { this.description     = description; }
    public void setDateToEnd(LocalDateTime dateToEnd)     { this.dateToEnd       = dateToEnd; }
    public void setPriority(Priority priority)            { this.priority        = priority; }
    public void setCategory(CategoryTask category)        { this.category        = category; }
    public void setStatus(StatusTask status)              { this.status          = status; }
    public void setAlarmEnabled(boolean alarmEnabled)     { this.alarmEnabled    = alarmEnabled; }
    public void setAlarmHoursPrior(int alarmHoursPrior)  { this.alarmHoursPrior = alarmHoursPrior; }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " | " + priority + " | " + status
               + " | Prazo: " + dateToEnd
               + " | Categoria: " + (category != null ? category.getName() : "Nenhuma")
               + (alarmEnabled ? " | ⏰ (" + alarmHoursPrior + "h antes)" : "");
    }
}
