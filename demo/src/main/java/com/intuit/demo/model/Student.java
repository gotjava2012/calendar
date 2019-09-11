package com.intuit.demo.model;

import java.util.List;

public class Student extends Person {
    private String id;
    private String description;

    public Student(String id, String name,  String description, List<Course> courses) {
        super(name, courses);
        this.id = id;
        this.description = description;
    }

    public Student(String id, String description) {
        this.id = id;
        this.description = description;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Student [id=%s, name=%s, description=%s, courses=%s]", id,
                super.getName(), description, super.getCourses());
    }
}
