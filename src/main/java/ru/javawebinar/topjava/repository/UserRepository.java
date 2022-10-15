package ru.javawebinar.topjava.repository;

public class UserRepository {
    private int caloriesPerDay;

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }
    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }
}
