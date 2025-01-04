package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private EntityManager em;

    public StudentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Student student) {
        try {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student) {
        try {
            em.getTransaction().begin();
            Student b = em.find(Student.class, student.getId());
            if (!b.equals(student)) {
                b = student;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Student student) {
        try {
            em.getTransaction().begin();
            em.remove(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentById(int id) {
        return em.find(Student.class, id);
    }

    @Override
    public List<Student> getAllStudents() {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
        return query.getResultList();
    }

    @Override
    public List<Student> getStudentsForDay(String day) {
        TypedQuery<Student> query = em.createQuery("SELECT c.students FROM ArtClass c WHERE c.dayOfWeek = :day", Student.class);
        query.setParameter("day", day);
        return query.getResultList();
    }
}
