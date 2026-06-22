package main.controller;

import main.model.CategoryTask;
import main.service.CategoryService;

import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void create(String name, String description) {
        categoryService.createCategory(name, description);
        System.out.println("✅ Categoria criada com sucesso!");
    }

    public void update(Integer id, String name, String description) {
        categoryService.updateCategory(id, name, description);
        System.out.println("✅ Categoria atualizada!");
    }

    public void remove(Integer id) {
        categoryService.removeCategory(id);
        System.out.println("🗑️  Categoria removida!");
    }

    public List<CategoryTask> listAll() { return categoryService.listAll(); }
    public CategoryTask findById(Integer id) { return categoryService.findById(id); }
}
