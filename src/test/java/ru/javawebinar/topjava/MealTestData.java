package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL1_ID = START_SEQ+3;
    public static final int MEAL2_ID = MEAL1_ID+1;
    public static final int MEAL3_ID = MEAL2_ID+1;
    public static final int NOT_FOUND = 10;

    public static final Meal userMeal1 = new Meal(MEAL1_ID, LocalDateTime.of(2022, 10, 24, 9,  00, 00), "Завтрак", 500);
    public static final Meal userMeal2 = new Meal(MEAL2_ID, LocalDateTime.of(2022, 10, 26, 12, 00, 00), "Обед", 500);
    public static final Meal userMeal3 = new Meal(MEAL3_ID, LocalDateTime.of(2022, 10, 26, 19, 00, 00), "Ужин", 500);

    public static final LocalDate inBetweenStartDate = LocalDate.of(2022, 10, 24);


    public static Meal getNew() {
        Meal meal = new Meal();
        meal.setCalories(500);
        meal.setDateTime(LocalDateTime.of(2022, 10, 24, 18, 00, 00));
        meal.setDescription("Новый ужин");

        return meal;
    }

    public static Meal getUpdated() {
        Meal updated = new Meal();
        updated.setId(MEAL1_ID);
        updated.setCalories(1000);
        updated.setDateTime(LocalDateTime.of(2022, 10, 24, 18, 00, 00));
        updated.setDescription("Ужин апдейт");

        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}