package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    public Student() {
    }
    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return null;
    }
    @Override
    public boolean equals(Object o){
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
