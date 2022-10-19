package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
@Service
public class MealService {
    private MealRepository repository;
    private UserService userService;

    public MealService(MealRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(userId, meal);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public Collection<MealTo> filter(int userId, LocalTime intervalStart, LocalTime intervalEnd){
        User user = userService.get(userId);
        return repository.filter(userId, user.getCaloriesPerDay(), intervalStart, intervalEnd);
    }
}