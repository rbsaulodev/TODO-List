package main.factory;

import main.model.CategoryTask;
import main.model.Task;
import main.model.enums.Priority;

import java.time.LocalDateTime;

public class TaskFactory {

    private TaskFactory() { /* utility class */ }

    public static Task createSimple(String name, String description,
                                    LocalDateTime dateToEnd, CategoryTask category) {
        return Task.createTask(name, description, dateToEnd,
                               Priority.MEDIUM, category, false, 0);
    }

    public static Task createFull(String name, String description,
                                  LocalDateTime dateToEnd, Priority priority,
                                  CategoryTask category, boolean alarmEnabled,
                                  int alarmHoursPrior) {
        return Task.createTask(name, description, dateToEnd, priority,
                               category, alarmEnabled, alarmHoursPrior);
    }

    public static Task createUrgent(String name, String description,
                                    LocalDateTime dateToEnd, CategoryTask category) {
        return Task.createTask(name, description, dateToEnd,
                               Priority.URGENT, category, true, 24);
    }
}
