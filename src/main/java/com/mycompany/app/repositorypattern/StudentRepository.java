package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Student;

import java.util.List;

public interface StudentRepository {
    public void add(Student student);
    public void update(Student student);
    public void remove(Student student);

    public Student getStudentById(int id);
    public List<Student> getAllStudents();
    public List<Student> getStudentsForDay(String day);
}
