package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal retMeal = service.get(MEAL3_ID, USER_ID);
        Meal expMeal = userMeal3;

        assertMatch(expMeal, retMeal);
    }

    @Test
    public void getMealFromOtherUserThrowsNotFoundEx() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, INCORRECT_USER));
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);

        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteMealFromOtherUserThrowsNotFoundEx() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, INCORRECT_USER));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> retMeals = service.getBetweenInclusive(inBetweenStartDate, inBetweenStartDate.plusDays(1), USER_ID);

        assertEquals(1, retMeals.size());
        MealTestData.assertMatch(retMeals, MealTestData.userMeal1);
    }

    @Test
    public void getBetweenInclusiveForMarginMeal() {
        List<Meal> retMeals = service.getBetweenInclusive(marginEndDate.minusDays(1), marginEndDate, USER_ID+1);

        assertEquals(0, retMeals.size());
    }

    @Test
    public void getAll() {
        List<Meal> retMeals = service.getAll(USER_ID);

        assertEquals(3, retMeals.size());
        MealTestData.assertMatch(retMeals, MealTestData.userMeal3, MealTestData.userMeal2, MealTestData.userMeal1);
    }

    @Test
    public void update() {
        Meal updMeal = service.get(MEAL1_ID, USER_ID);
        updMeal.setCalories(1000);
        updMeal.setDateTime(LocalDateTime.of(2022, 10, 24, 18, 0, 0));
        updMeal.setDescription("Ужин апдейт");
        service.update(updMeal, USER_ID);

        Meal controlMeal = MealTestData.getUpdated();

        Meal retMeal = service.get(MEAL1_ID, USER_ID);
        MealTestData.assertMatch(retMeal, controlMeal);
        MealTestData.assertMatch(updMeal, controlMeal);
    }


    @Test
    public void updateMealFromOtherUserThrowsNotFoundEx() {
        Meal updMeal = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updMeal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal mealToCreate = getNew();

        Meal created = service.create(mealToCreate, USER_ID);
        Integer newId = created.getId();
        Meal controlMeal = getNew();
        controlMeal.setId(newId);

        MealTestData.assertMatch(created, controlMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), controlMeal);
    }

    @Test
    public void createMealWithDuplicateDateTimeThrows() {
        Meal mealToCreate = MealTestData.getNew();
        mealToCreate.setDateTime(MealTestData.userMeal1.getDateTime());

        assertThrows(DataAccessException.class, () -> service.create(mealToCreate, USER_ID));
    }
}