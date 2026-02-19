package service;

import model.CategoryTask;
import model.Task;
import model.enums.Priority;
import model.enums.StatusTask;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoManager {
    private List<Task> tasks = new ArrayList<>();
    private final String path = "src/data/tasks.csv";

    private void addTask(Task task) {
        tasks.add(task);
        Collections.sort(tasks);
    }

    public void checkAlarms() {
        System.out.println("\n VERIFICANDO ALARMES ATIVOS...");
        boolean hasAlarms = false;
        for (Task task : tasks) {
            if (task.shouldTriggerAlarm()) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                System.out.println("⚠️ ALERTA: '" + task.getName() + "' agendada para " + task.getDateToEnd().format(fmt));
                hasAlarms = true;
            }
        }
        if (!hasAlarms) System.out.println("Nenhum alarme disparado no momento.");
    }


    // Sistema de Memória
    private void saveAllDataCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("id,name,description,dateToEnd,priority,category,status,alarmEnabled,alarmHoursPrior");
            for (Task t : tasks) {
                pw.println(t.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public void loadTasksCSV(CategoryManager catManager) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                CategoryTask cat = catManager.findById(Integer.parseInt(d[5].trim()));
                if (cat != null) {
                    Task task = Task.createTask(
                            d[1], d[2],
                            LocalDateTime.parse(d[3]),
                            Priority.fromCode(Integer.parseInt(d[4])),
                            cat,
                            Boolean.parseBoolean(d[7]),
                            Integer.parseInt(d[8])
                    );
                    task.setStatus(StatusTask.valueOf(d[6].trim()));
                    this.tasks.add(task);
                }
            }
            Collections.sort(tasks);
            checkAlarms();
        } catch (IOException e) {
            System.out.println("Arquivo de tarefas não encontrado.");
        }
    }

    // CRUD
    public void createTask(String name, String description, LocalDateTime dateToEnd, Priority priority, CategoryTask category, boolean alarmOn, int hours) {
        Task newTask = Task.createTask(name, description, dateToEnd, priority, category, alarmOn, hours);
        tasks.add(newTask);
        Collections.sort(tasks);
        saveAllDataCSV();
        System.out.println("Tarefa criada com sucesso!");
    }

    public void updateTask(Integer id, String name, String description, LocalDateTime date, Priority prio, StatusTask status, Boolean alarm, Integer hours) {
        tasks.stream().filter(t -> t.getId().equals(id)).findFirst().ifPresent(t -> {
            t.updateData(name, description, date, prio, status, alarm, hours);
            saveAllDataCSV();
        });
    }

    public void removeTask(Integer id) {
        if (tasks.removeIf(t -> t.getId().equals(id))) {
            System.out.println("Removida.");
            saveAllDataCSV();
        } else {
            System.out.println("Não encontrada.");
        }
    }

    public Task findById(Integer id) {
        return tasks.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        }

    public void listAllTasks() {
        if (tasks.isEmpty()) System.out.println("Lista vazia.");
        else tasks.forEach(System.out::println);
    }

    public void listTaskByNumber(Integer number){
        tasks.stream()
                .filter(t -> t.getStatus() == StatusTask.fromCode(number))
                .forEach(System.out::println);
    }
}