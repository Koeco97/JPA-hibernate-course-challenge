package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.Map;

public class ReviewRepositoryImpl implements ReviewRepository {

    private EntityManager em;

    public ReviewRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Review review) {
        try {
            em.getTransaction().begin();
            em.persist(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Review review) {
        try {
            em.getTransaction().begin();
            Review b = em.find(Review.class, review.getId());
            if (!b.equals(review)) {
                b = review;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Review review) {
        try {
            em.getTransaction().begin();
            em.remove(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Double getAvgRatingForTeacher(String teacherName) {
        TypedQuery<Double> query = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.teacher.name = :name", Double.class);
        query.setParameter("name", teacherName);
        return query.getSingleResult();
    }

    @Override
    public Map<String, Double> getAvgRatingsByTeachers() {
        Map<String, Double> avgRatings = new HashMap<>();
        String queryString = """
                    SELECT t.name, AVG(r.rating) 
                    FROM Review r 
                    INNER JOIN r.teacher t
                    GROUP BY t.name 
                    """;

        TypedQuery<Object[]> query = em.createQuery(queryString, Object[].class);
        query.getResultList().forEach(o -> avgRatings.put((String) o[0], (Double) o[1]));
        return avgRatings;
    }
}
