package api.model;

import java.util.List;

public class Pet {
    public long id;
    public Category category;
    public String name;
    public List<String> photoUrls;
    public List<Tag> tags;
    public String status; // "available", "pending", "sold"

    public Pet() {}

    public Pet(long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}
