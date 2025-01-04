package com.mycompany.app;

import com.mycompany.app.entities.ArtClass;
import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Student;
import com.mycompany.app.entities.Teacher;
import com.mycompany.app.repositorypattern.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.checkerframework.checker.units.qual.A;

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
            // printListOfAllStudents(emf);
            // printListOfStudentsAttendingClassesOn(emf, "Monday");
            // printAvgRatingForTeacherName(emf, "White");
            // printAvgRatingForTeachers(emf);
            // printAvgRatingForTeachersDesc(emf);
            // printAvgRatingForTeachersGt3Desc(emf);
            useRepository(emf);
        }
    }

    private static void create(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager(); // Represents the persistence context
        try (em) {
            em.getTransaction().begin();

            Student student = new Student();
            student.setName("John");
            em.persist(student);

            em.getTransaction().commit();
        }
    }

    private static void update(EntityManagerFactory emf) {
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

    private static void remove(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();
            Student student = em.find(Student.class, 1L);
            em.remove(student);
            em.getTransaction().commit();
        }
    }

    private static void oneToOneRelationship(EntityManagerFactory emf) {
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

    private static void oneToManyRelationship(EntityManagerFactory emf) {
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

    private static void manyToManyRelationship(EntityManagerFactory emf) {
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

    private static void printListOfAllStudents(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            List<Student> students = query.getResultList();
            for (Student s : students) {
                System.out.println(s);
            }
            em.getTransaction().commit();
        }
    }

    private static void printListOfStudentsAttendingClassesOn(EntityManagerFactory emf, String day) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

            TypedQuery<Student> query = em.createQuery("SELECT c.students FROM ArtClass c WHERE c.dayOfWeek = :day", Student.class);
            query.setParameter("day", day);
            List<Student> students = query.getResultList();
            for (Student s : students) {
                System.out.println(s);
            }

            em.getTransaction().commit();
        }
    }

    private static void printAvgRatingForTeacherName(EntityManagerFactory emf, String name) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

            TypedQuery<Double> query = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.teacher.name = :name", Double.class);
            query.setParameter("name", name);
            Double avgRating = query.getSingleResult();
            System.out.println("Average rating for " + name + ": " + avgRating);

            em.getTransaction().commit();
        }
    }

    private static void printAvgRatingForTeachers(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

//            String queryString = """
//                    SELECT r.teacher.name, AVG(r.rating)
//                    FROM Review r
//                    GROUP BY r.teacher.name
//                    """;

            String queryString = """
                    SELECT t.name, AVG(r.rating) 
                    FROM Review r 
                    INNER JOIN r.teacher t
                    GROUP BY t.name 
                    """;

            TypedQuery<Object[]> query = em.createQuery(queryString, Object[].class);
            query.getResultList().forEach(
                    o -> System.out.println(
                            "Average rating by teacher " + o[0] + ": " + o[1]
                    )
            );

            em.getTransaction().commit();
        }
    }

    private static void printAvgRatingForTeachersDesc(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

//            String queryString = """
//                    SELECT r.teacher.name, AVG(r.rating) ar
//                    FROM Review r
//                    GROUP BY r.teacher.name
//                    ORDER BY ar DESC
//                    """;

            String queryString = """
                    SELECT t.name, AVG(r.rating) 
                    FROM Review r 
                    INNER JOIN r.teacher t
                    GROUP BY t.name 
                    ORDER BY AVG(r.rating) DESC
                    """;

            TypedQuery<Object[]> query = em.createQuery(queryString, Object[].class);
            query.getResultList().forEach(
                    o -> System.out.println(
                            "Average rating by teacher " + o[0] + ": " + o[1]
                    )
            );

            em.getTransaction().commit();
        }
    }

    private static void printAvgRatingForTeachersGt3Desc(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try (em) {
            em.getTransaction().begin();

//            String queryString = """
//                    SELECT r.teacher.name, AVG(r.rating) ar
//                    FROM Review r
//                    GROUP BY r.teacher.name
//                    HAVING AVG(r.rating) > 3
//                    ORDER BY ar DESC
//                    """;

            String queryString = """
                    SELECT t.name, AVG(r.rating) 
                    FROM Review r 
                    INNER JOIN r.teacher t
                    GROUP BY t.name 
                    HAVING AVG(r.rating) > 3
                    ORDER BY AVG(r.rating) DESC
                    """;

            TypedQuery<Object[]> query = em.createQuery(queryString, Object[].class);
            query.getResultList().forEach(
                    o -> System.out.println(
                            "Average rating by teacher " + o[0] + ": " + o[1]
                    )
            );

            em.getTransaction().commit();
        }
    }

    private static void useRepository(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        try(em) {
            ArtClassRepository artClassRepository = new ArtClassRepositoryImpl(em);
            ArtClass artClass = new ArtClass();
            artClass.setName("ArtClassTestCreation");
            artClassRepository.add(artClass);

            List<ArtClass> artClasses = artClassRepository.getClassByName("ArtClassTestCreation");
            artClasses.forEach(aClass -> {
                aClass.setName("ArtClassTestUpdate");
                artClassRepository.update(aClass);
            });

            List<ArtClass> artClassesRemove = artClassRepository.getClassByName("ArtClassTestUpdate");
            artClassesRemove.forEach(aClass -> {
                artClassRepository.remove(aClass);
            });

            ReviewRepository reviewRepository = new ReviewRepositoryImpl(em);
            Double avgRating = reviewRepository.getAvgRatingForTeacher("White");
            System.out.println("Avg rating for White: " + avgRating);

            reviewRepository.getAvgRatingsByTeachers().forEach((teacherName, avgRatingByTeacher) -> System.out.println("Avg rating for Teacher: " + teacherName + ": " + avgRatingByTeacher));

            StudentRepository studentRepository = new StudentRepositoryImpl(em);
            int studentId = 1;
            Student student = studentRepository.getStudentById(studentId);
            System.out.println("Student with studentId " + studentId + ": " + student);

            studentRepository.getAllStudents().forEach(System.out::println);
            studentRepository.getStudentsForDay("Monday").forEach(System.out::println);

            TeacherRepository teacherRepository = new TeacherRepositoryImpl(em);
            int teacherId = 1;
            Teacher teacher = teacherRepository.getTeacherById(teacherId);
            System.out.println("Teacher with teacherId " + teacherId + ": " + teacher);
        }
    }

}