package org.example.model;

public class Student {
    private String name;
    private String surName;
    private Integer age;

    public Student() {
    }

    public Student(String name, String surName, Integer age) {
        this.name = name;
        this.surName = surName;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student: " + '\n' +
                "name: " + name + '\n' +
                "surName: " + surName + '\n' +
                "age: " + age+ '\n';
    }
}
