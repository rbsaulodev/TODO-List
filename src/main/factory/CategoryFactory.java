package main.factory;

import main.model.CategoryTask;

public class CategoryFactory {

    private CategoryFactory() { /* utility class */ }

    public static CategoryTask create(String name, String description) {
        return CategoryTask.create(name, description);
    }
}
