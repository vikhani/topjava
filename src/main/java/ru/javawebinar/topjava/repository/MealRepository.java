package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.ArrayList;

public class MealRepository {
    private List<Meal> meals = new ArrayList<>();

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }
}
