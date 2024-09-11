package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Class {

    private String name;
    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();

    public Class() {
    }

    public Class(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    @Override
    public String toString() {
        return "Class: " +
                "name: " + name + '\n' +
                "_students: " + students + '\n' +
                "_subjects: " + subjects;
    }
}
