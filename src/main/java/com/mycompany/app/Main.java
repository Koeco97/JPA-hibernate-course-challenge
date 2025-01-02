package com.mycompany.app;

import com.mycompany.app.entities.ArtClass;
import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Student;
import com.mycompany.app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
  public static void main(String[] args) {
      try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("artclass_persistence_unit")) {
          // create(emf);
          // update(emf);
          // attachAndDetach(emf);
          // remove(emf);
          // oneToOneRelationship(emf);
          // oneToManyRelationship(emf);
          // manyToManyRelationship(emf);
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

    private static void oneToOneRelationship(EntityManagerFactory emf){
      EntityManager em = emf.createEntityManager();
      try (em) {
          em.getTransaction().begin();
          ArtClass artClass = new ArtClass();
          artClass.setName("Oil painting");
          artClass.setDayOfWeek("Monday");
          Teacher teacher = new Teacher();
          teacher.setName("John");
          artClass.setTeacher(teacher);
          em.persist(artClass);
          em.persist(teacher);
          em.getTransaction().commit();
      }
    }

    private static void oneToManyRelationship(EntityManagerFactory emf){
      EntityManager em = emf.createEntityManager();
      try (em) {
          em.getTransaction().begin();
          Teacher teacher = em.find(Teacher.class, 1L);
          Review review1 = new Review();
          review1.setRating(10);
          review1.setComment("Great teacher!");
          review1.setTeacher(teacher);
          Review review2 = new Review();
          review2.setRating(9);
          review2.setComment("Good teacher");
          review2.setTeacher(teacher);
          teacher.setReviews(List.of(review1, review2));
          em.persist(teacher);
          em.getTransaction().commit();
      }
    }

    private static void manyToManyRelationship(EntityManagerFactory emf){
      EntityManager em = emf.createEntityManager();
      try (em) {
          em.getTransaction().begin();
          ArtClass artClass1 = em.find(ArtClass.class, 2L);
          ArtClass artClass2 = new ArtClass();
          artClass2.setName("water colors class");
          artClass2.setDayOfWeek("Wednesday");
          Teacher teacher = new Teacher();
          teacher.setName("Alice");
          artClass2.setTeacher(teacher);

          Student student1 = em.find(Student.class, 2L);
          Student student2 = new Student();
          student2.setName("Peter");
          artClass1.setStudents(List.of(student1, student2));
          artClass2.setStudents(List.of(student1));

          em.persist(teacher);
          em.persist(student2);
          em.persist(artClass1);
          em.persist(artClass2);
          em.getTransaction().commit();
      }
    }
}