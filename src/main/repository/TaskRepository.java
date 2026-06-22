package main.repository;


import main.model.CategoryTask;
import main.model.Task;
import main.model.enums.Priority;
import main.model.enums.StatusTask;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements Repository<Task, Integer> {

    private static final String FILE_PATH = "data/tasks.csv";
    private static final String HEADER    = "id,name,description,dateToEnd,priority,category,status,alarmEnabled,alarmHoursPrior";

    private final List<Task> tasks = new ArrayList<>();
    private final CategoryRepository categoryRepository;

    public TaskRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void save(Task task) {
        tasks.add(task);
        saveAll(tasks);
    }

    @Override
    public void update(Task task) {
        saveAll(tasks);
    }

    @Override
    public void delete(Integer id) {
        tasks.removeIf(t -> t.getId().equals(id));
        saveAll(tasks);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    @Override
    public void saveAll(List<Task> entities) {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println(HEADER);
            entities.forEach(t -> pw.println(t.toCSV()));
        } catch (IOException e) {
            System.err.println("Erro ao salvar tasks.csv: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        tasks.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                Integer      id             = Integer.parseInt(p[0]);
                String       name           = p[1];
                String       description    = p[2];
                LocalDateTime dateToEnd     = p[3].isBlank() ? null : LocalDateTime.parse(p[3]);
                Priority priority      = Priority.fromCode(Integer.parseInt(p[4]));
                CategoryTask category      = p[5].isBlank() ? null
                    : categoryRepository.findById(Integer.parseInt(p[5])).orElse(null);
                StatusTask status        = StatusTask.fromCode(Integer.parseInt(p[6]));
                boolean       alarmEnabled  = Boolean.parseBoolean(p[7]);
                int           alarmHours    = Integer.parseInt(p[8]);
                tasks.add(new Task(id, name, description, dateToEnd, priority,
                                   category, status, alarmEnabled, alarmHours));
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar tasks.csv: " + e.getMessage());
        }
    }

    /** Acesso direto à lista mutável interna (uso pelo service) */
    public List<Task> getTasksMutable() { return tasks; }
}
