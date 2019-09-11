package com.intuit.demo.model;

import java.util.List;

public class Instructor extends Person {
    private String id;
    private String certifications;

    public Instructor(){}

    public Instructor(String id, String certifications) {
        this.id = id;
        this.certifications = certifications;
    }

    public Instructor(String id, String name, String certifications, List<Course> courses) {
        super(name, courses);
        this.id = id;
        this.certifications = certifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    @Override
    public String toString() {
        return String.format(
                "Instructor [id=%s, name=%s, certifications=%s, courses=%s]", id,
                super.getName(),certifications, super.getCourses());
    }
}
