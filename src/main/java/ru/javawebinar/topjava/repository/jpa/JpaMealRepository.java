package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.dao.support.DataAccessUtils;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;
import java.time.LocalDateTime;

import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        if(meal.isNew()){
            meal.setUser(ref);
            em.persist(meal);
            return meal;
        } else {
            meal.setUser(ref);
            Meal updatedMealRef = em.getReference(Meal.class, meal.getId());
            return updatedMealRef.getUser().getId() == userId ? em.merge(meal) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal m = em.find(Meal.class, id);
        if (m == null || m.getUser().getId() != userId) {
            return false;
        }

        em.remove(m);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.GET)
                .setParameter(Meal.ID_PARAM, id)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .getResultList();

        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALFOPEN)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .setParameter(Meal.START_TIME_PARAM, startDateTime)
                .setParameter(Meal.END_TIME_PARAM, endDateTime)
                .getResultList();
    }
}