package main.observer;


import main.model.Task;

public class AlarmObserver implements TaskObserver {

    @Override
    public void onTaskCreated(Task task) {
        checkAlarm(task);
    }

    @Override
    public void onTaskUpdated(Task task) {
        checkAlarm(task);
    }

    @Override
    public void onTaskRemoved(Integer taskId) {
    }

    private void checkAlarm(Task task) {
        if (task.shouldTriggerAlarm()) {
            System.out.println("⏰ ALARME: A tarefa \"" + task.getName()
                + "\" vence em menos de " + task.getAlarmHoursPrior() + " hora(s)!");
        }
    }
}
