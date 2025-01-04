package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Student;
import com.mycompany.app.entities.Teacher;
import jakarta.persistence.EntityManager;

public class TeacherRepositoryImpl implements TeacherRepository {
    private EntityManager em;

    public TeacherRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Teacher teacher) {
        try {
            em.getTransaction().begin();
            em.persist(teacher);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Teacher teacher) {
        try {
            em.getTransaction().begin();
            Teacher b = em.find(Teacher.class, teacher.getId());
            if (!b.equals(teacher)) {
                b = teacher;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Teacher teacher) {
        try {
            em.getTransaction().begin();
            em.remove(teacher);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Teacher getTeacherById(int id) {
        return em.find(Teacher.class, id);
    }
}
