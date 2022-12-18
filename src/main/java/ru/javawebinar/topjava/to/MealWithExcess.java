package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.beans.ConstructorProperties;

import java.io.Serial;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealWithExcess extends BaseTo {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String description;

    @NotNull
    @Range(min = 1, max = 5000)
    private int calories;

    private boolean excess;

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealWithExcess(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public MealWithExcess() {
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

    public boolean isExcess() {
        return excess;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealWithExcess mealWithExcess = (MealWithExcess) o;
        return calories == mealWithExcess.calories &&
                excess == mealWithExcess.excess &&
                Objects.equals(id, mealWithExcess.id) &&
                Objects.equals(dateTime, mealWithExcess.dateTime) &&
                Objects.equals(description, mealWithExcess.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
