package ru.javawebinar.topjava.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.javawebinar.topjava.util.ValidationUtil.assureCorrectUser;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        assureCorrectUser(userId, "Can't access meal with id=" + id);
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void delete(int id, int userId) {
        assureCorrectUser(userId, "Can't access meal with id=" + id);
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) {
        assureCorrectUser(userId, "Can't access meal with id=" + meal.getId());
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public Meal create(Meal meal, int userId) {
        assureCorrectUser(userId, "Can't create for another user");
        return repository.save(meal, userId);
    }
}