package main.repository;


import main.model.CategoryTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository implements Repository<CategoryTask, Integer> {

    private static final String FILE_PATH = "data/categories.csv";
    private static final String HEADER    = "id,name,description";

    private final List<CategoryTask> categories = new ArrayList<>();

    @Override
    public void save(CategoryTask category) {
        categories.add(category);
        saveAll(categories);
    }

    @Override
    public void update(CategoryTask category) {
        saveAll(categories);
    }

    @Override
    public void delete(Integer id) {
        categories.removeIf(c -> c.getId().equals(id));
        saveAll(categories);
    }

    @Override
    public Optional<CategoryTask> findById(Integer id) {
        return categories.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<CategoryTask> findAll() {
        return List.copyOf(categories);
    }

    @Override
    public void saveAll(List<CategoryTask> entities) {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println(HEADER);
            entities.forEach(c -> pw.println(c.toCSV()));
        } catch (IOException e) {
            System.err.println("Erro ao salvar categories.csv: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        categories.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                categories.add(new CategoryTask(
                    Integer.parseInt(p[0]), p[1], p.length > 2 ? p[2] : ""
                ));
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar categories.csv: " + e.getMessage());
        }
    }

    public Optional<CategoryTask> findByName(String name) {
        return categories.stream()
            .filter(c -> c.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    public List<CategoryTask> getCategoriesMutable() { return categories; }
}
