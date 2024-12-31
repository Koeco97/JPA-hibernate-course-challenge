package com.mycompany.app;

import com.mycompany.app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
  public static void main(String[] args) {
      try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("artclass_persistence_unit")) {
          // create(emf);
          // update(emf);
          // attachAndDetach(emf);
          remove(emf);
      }
  }

    private static void create(EntityManagerFactory emf){
        EntityManager em = emf.createEntityManager(); // Represents the persistence context
        try (em) {
            em.getTransaction().begin();

            Student student = new Student();
            student.setName("John");
            em.persist(student);

            em.getTransaction().commit();
        }
    }

    private static void update(EntityManagerFactory emf){
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();
            Student student = em.find(Student.class, 1L);  // 1L - primary key
            student.setName("Peter");
            em.getTransaction().commit();
        }
    }

    private static void attachAndDetach(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();
            Student student = new Student();
            student.setName("Mary");
            // em.merge(student);
            em.detach(student);
            student.setName("Alice");
            em.getTransaction().commit();
        }
    }

    private static void remove(EntityManagerFactory emf){
      EntityManager em = emf.createEntityManager();
      try (em) {
          em.getTransaction().begin();
          Student student = em.find(Student.class, 1L);
          em.remove(student);
          em.getTransaction().commit();
      }
    }
}