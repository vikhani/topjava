package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }

        Meal newMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);

        if (newMeal == null) {
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            newMeal = meal;
        }

        return newMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (checkMealAvailable(id, userId)) {
            return repository.remove(id) != null;
        }

        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (checkMealAvailable(id, userId)) {
            return repository.get(id);
        }

        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal->meal.getUserId() == userId)
                .sorted()
                .collect(Collectors.toList());
    }

    private boolean checkMealAvailable(int id, int userId) {
        Meal inspectedMeal = repository.get(id);
        return inspectedMeal != null && inspectedMeal.getUserId() == userId;
    }
}

