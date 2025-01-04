package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.ArtClass;
import com.mycompany.app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ArtClassRepositoryImpl implements ArtClassRepository {

    private EntityManager em;

    public ArtClassRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public void add(ArtClass artClass) {
        try {
            em.getTransaction().begin();
            em.persist(artClass);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ArtClass artClass) {
        try {
            em.getTransaction().begin();
            ArtClass b = em.find(ArtClass.class, artClass.getId());
            if (!b.equals(artClass)) {
                b = artClass;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(ArtClass artClass) {
        try {
            em.getTransaction().begin();
            em.remove(artClass);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArtClass getClassById(int id) {
        return em.find(ArtClass.class, id);
    }

    @Override
    public List<ArtClass> getClassByName(String name) {
        TypedQuery<ArtClass> query = em.createQuery("SELECT c FROM ArtClass c WHERE c.name = :name", ArtClass.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
