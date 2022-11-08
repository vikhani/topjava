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
        if (meal.isNew()) {
            User ref = em.getReference(User.class, userId);
            meal.setUser(ref);
            em.persist(meal);
            return meal;
        } else {
            return meal.getUser().getId() == userId ? em.merge(meal) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter(Meal.ID_PARAM, id)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter(Meal.ID_PARAM, id)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .getResultList();

        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALFOPEN, Meal.class)
                .setParameter(Meal.USER_ID_PARAM, userId)
                .setParameter(Meal.START_TIME_PARAM, startDateTime)
                .setParameter(Meal.END_TIME_PARAM, endDateTime)
                .getResultList();
    }
}