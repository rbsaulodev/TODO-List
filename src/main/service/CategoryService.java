package main.service;

import main.factory.CategoryFactory;
import main.model.CategoryTask;
import main.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public void createCategory(String name, String description) {
        CategoryTask cat = CategoryFactory.create(name, description);
        repository.save(cat);
    }

    public void updateCategory(Integer id, String name, String description) {
        CategoryTask cat = findById(id);
        cat.updateData(name, description);
        repository.update(cat);
    }

    public void removeCategory(Integer id) {
        findById(id); // valida existência
        repository.delete(id);
    }

    public CategoryTask findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + id));
    }

    public Optional<CategoryTask> findByName(String name) {
        return repository.findByName(name);
    }

    public List<CategoryTask> listAll() {
        return repository.findAll();
    }

    public void load() {
        repository.loadAll();
    }
}
