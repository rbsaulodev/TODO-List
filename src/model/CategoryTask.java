package model;

public class CategoryTask {
    private static int idCounter = 0;
    private Integer id;
    private String name;
    private String description;

    public CategoryTask(String name, String description) {
        this.id = ++idCounter;
        this.name = name;
        this.description = description;
    }

    public static CategoryTask createCategoryTask(String name, String description) {
        return new CategoryTask(name, description);
    }

    public void updateData(String name, String description) {
        if (name != null && !name.isBlank()) this.name = name;
        if (description != null && !description.isBlank()) this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
