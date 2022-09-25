package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.util.*;
import java.util.stream.Collectors;

import java.time.Month;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import static java.util.stream.Collectors.groupingBy;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0,  0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("Filtered meals by cycles:");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println("Filtered meals by streams:");
        List<UserMealWithExcess> mealsToWithStreams = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToWithStreams.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals,
                                                            LocalTime startTime,
                                                            LocalTime endTime,
                                                            int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();

        Map<LocalDate, Integer> mealsByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            mealsByDate.merge(date, meal.getCalories(), Integer::sum);
        }

        for (UserMeal meal : meals) {
            LocalDateTime date = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(date.toLocalTime(), startTime, endTime)
                    && mealsByDate.get(date.toLocalDate()) > caloriesPerDay) {
                result.add(
                        new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true)
                );
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values()
                .stream()
                .filter(mealsByDay -> mealsByDay.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay)
                .flatMap(List::stream)
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true))
                .collect(Collectors.toList());
    }
}
