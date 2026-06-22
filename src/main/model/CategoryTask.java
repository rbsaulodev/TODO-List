package main.model;

import java.util.Objects;


public class CategoryTask {

    private static int idCounter = 1;

    private final Integer id;
    private String        name;
    private String        description;

    private CategoryTask(String name, String description) {
        this.id          = idCounter++;
        this.name        = Objects.requireNonNull(name, "Nome não pode ser nulo");
        this.description = description;
    }

    public CategoryTask(Integer id, String name, String description) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        if (id >= idCounter) idCounter = id + 1;
    }

    public static CategoryTask create(String name, String description) {
        return new CategoryTask(name, description);
    }

    public void updateData(String name, String description) {
        if (name        != null && !name.isBlank())        this.name        = name;
        if (description != null && !description.isBlank()) this.description = description;
    }

    public Integer getId()          { return id; }
    public String  getName()        { return name; }
    public String  getDescription() { return description; }
    public void    setName(String name)               { this.name = name; }
    public void    setDescription(String description) { this.description = description; }

    public String toCSV() {
        return id + "," + name + "," + description;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " — " + description;
    }
}
