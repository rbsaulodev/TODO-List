package main.observer;

import main.model.Task;

public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskUpdated(Task task);
    void onTaskRemoved(Integer taskId);
}
