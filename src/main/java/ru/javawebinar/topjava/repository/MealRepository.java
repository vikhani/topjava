package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.MealTo;

import java.util.List;
import java.util.ArrayList;

public class MealRepository {
    private List<MealTo> meals = new ArrayList<>();

    private static final int CALORIES_PER_DAY = 2000;

    public void setMeals(List<MealTo> meals) {
        this.meals = meals;
    }

    public List<MealTo> getMeals() {
        return new ArrayList<>(meals);
    }

    public int getCaloriesPerDay() {
        return CALORIES_PER_DAY;
    }
}
