package model;

import model.enums.Priority;
import model.enums.StatusTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Task implements Comparable<Task> {
    private static int idCounter = 0;
    private final Integer id;
    private String name;
    private String description;
    private LocalDateTime dateToEnd;
    private Priority priority;
    private CategoryTask category;
    private StatusTask status;
    private boolean alarmEnabled;
    private int alarmHoursPrior;

    private Task(String name, String description, LocalDateTime dateToEnd, Priority priority, CategoryTask category, StatusTask status, boolean alarmEnabled, int alarmHoursPrior) {
        this.id = ++idCounter;
        this.name = name;
        this.description = description;
        this.dateToEnd = dateToEnd;
        this.priority = priority;
        this.category = category;
        this.status = status;
        this.alarmEnabled = alarmEnabled;
        this.alarmHoursPrior = alarmHoursPrior;
    }

    public static Task createTask(String name, String description, LocalDateTime dateToEnd, Priority priority, CategoryTask category, boolean alarmEnabled, int alarmHoursPrior) {
        return new Task(name, description, dateToEnd, priority, category, StatusTask.TODO, alarmEnabled, alarmHoursPrior);
    }

    public void updateData(String name, String description, LocalDateTime dateToEnd, Priority priority, StatusTask status, Boolean alarmEnabled, Integer alarmHoursPrior) {
        if (name != null && !name.isBlank()) this.name = name;
        if (description != null && !description.isBlank()) this.description = description;
        if (dateToEnd != null) this.dateToEnd = dateToEnd;
        if (priority != null) this.priority = priority;
        if (status != null) this.status = status;
        if (alarmEnabled != null) this.alarmEnabled = alarmEnabled;
        if (alarmHoursPrior != null) this.alarmHoursPrior = alarmHoursPrior;
    }

    public boolean shouldTriggerAlarm() {
        if (!alarmEnabled || status == StatusTask.DONE) return false;

        LocalDateTime now = LocalDateTime.now();

        long hoursUntil = ChronoUnit.HOURS.between(now, dateToEnd);
        return hoursUntil >= 0 && hoursUntil <= alarmHoursPrior;
    }

    @Override
    public int compareTo(Task other) {
        return other.getPriority().getNumber().compareTo(this.priority.getNumber());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String alarmInfo = alarmEnabled ? String.format(" [Alarme: %dh antes]", alarmHoursPrior) : " [Alarme: Off]";
        return String.format("ID: %d | %s [%s] | Prioridade: %s | Fim: %s | Categoria: %s%s",
                id, status, name, priority.getNumber(), dateToEnd.format(formatter), category.getName(), alarmInfo);
    }

    public String toCSV() {
        return String.format("%d,%s,%s,%s,%d,%d,%s,%b,%d",
                id, name, description, dateToEnd, priority.getNumber(), category.getId(), status.name(), alarmEnabled, alarmHoursPrior);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateToEnd() {
        return dateToEnd;
    }

    public void setDateToEnd(LocalDateTime dateToEnd) {
        this.dateToEnd = dateToEnd;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public CategoryTask getCategory() {
        return category;
    }

    public void setCategory(CategoryTask category) {
        this.category = category;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }
}
