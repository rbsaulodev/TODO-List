package service;

import model.CategoryTask;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private List<CategoryTask> categories = new ArrayList<>();
    private final String path = "src/data/categories.csv";

    private void addCategory(CategoryTask category) {
        categories.add(category);
    }

    public void saveCategoriesCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("id,name,description");
            for (CategoryTask c : categories) {
                pw.println(c.getId() + "," + c.getName() + "," + c.getDescription());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar categorias: " + e.getMessage());
        }
    }

    public void loadCategoriesCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                CategoryTask cat = new CategoryTask(data[1], data[2]);
                this.categories.add(cat);
            }
        } catch (IOException e) {
            System.out.println("Arquivo de categorias não encontrado. Começando do zero.");
        }
    }

    //CRUD
    public void createCategory(String name, String description) {
        CategoryTask newCategory = CategoryTask.createCategoryTask(name, description);
        this.addCategory(newCategory);
        saveCategoriesCSV();
        System.out.println("Nova tarefa criada com ID: " + newCategory.getId());
    }

    public void updateCategory(Integer id, String name, String description) {
        categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        category -> {
                            category.updateData(name, description);
                            saveCategoriesCSV();
                            System.out.println("Categoria atualizada!");
                        },
                        () -> System.out.println("Categoria não encontrada.")
                );
    }

    public void removeCategory(Integer id) {
        if (categories.removeIf(t -> t.getId().equals(id))) {
            saveCategoriesCSV();
            System.out.println("Removida.");
        } else {
            System.out.println("Não encontrada.");
        }
    }

    public void listAllCategories() {
        if (categories.isEmpty()) System.out.println("Lista vazia.");
        else categories.forEach(System.out::println);
    }

    public CategoryTask findById(Integer id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public CategoryTask findByName(String name) {
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }
}
