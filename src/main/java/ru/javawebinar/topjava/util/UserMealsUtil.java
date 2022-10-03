package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.util.*;
import java.util.stream.Collectors;

import java.time.Month;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

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
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesByDate.get(meal.getDate()) > caloriesPerDay)
                );
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(UserMeal::getDate), Map::values))
                .stream()
                .map(mealsByDay -> {
                    boolean isExcessiveDay = mealsByDay.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return mealsByDay.stream()
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                            .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcessiveDay))
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
