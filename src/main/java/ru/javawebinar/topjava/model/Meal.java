package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity implements Comparable<Meal> {
    private Integer userId;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;


    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, null, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, Integer userId, int calories) {
        super(id);
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.userId = userId;
        this.calories = calories;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", userId=" + userId +
                '}';
    }

    @Override
    public int compareTo(Meal m) {
        return this.getDateTime().compareTo(m.getDateTime());
    }
}
