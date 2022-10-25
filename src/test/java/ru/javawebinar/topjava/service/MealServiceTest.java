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

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.inBetweenStartDate;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateTestDB.sql", config = @SqlConfig(encoding = "UTF-8"))
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
        Meal retMeal = service.get(MealTestData.MEAL3_ID, MealTestData.USER_ID);
        Meal expMeal = MealTestData.userMeal3;

        MealTestData.assertMatch(expMeal, retMeal);
    }

    @Test
    public void getMealFromOtherUserThrowsNotFoundEx() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL1_ID, MealTestData.NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.MEAL1_ID, MealTestData.USER_ID);

        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL1_ID, MealTestData.USER_ID));
    }

    @Test
    public void deleteMealFromOtherUserThrowsNotFoundEx() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.MEAL1_ID, MealTestData.NOT_FOUND));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> retMeals = service.getBetweenInclusive(inBetweenStartDate, inBetweenStartDate.plusDays(1), MealTestData.USER_ID);

        assertEquals(1, retMeals.size());
        MealTestData.assertMatch(retMeals, MealTestData.userMeal1);
    }

    @Test
    public void getAll() {
        List<Meal> retMeals = service.getAll(MealTestData.USER_ID);

        assertEquals(3, retMeals.size());
        MealTestData.assertMatch(retMeals, MealTestData.userMeal3, MealTestData.userMeal2, MealTestData.userMeal1);
    }

    @Test
    public void update() {
        Meal updMeal = MealTestData.getUpdated();
        service.update(updMeal, MealTestData.USER_ID);

        Meal retMeal = service.get(MealTestData.MEAL1_ID, MealTestData.USER_ID);
        MealTestData.assertMatch(updMeal, retMeal);
    }


    @Test
    public void updateMealFromOtherUserThrowsNotFoundEx() {
        Meal updMeal = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updMeal, MealTestData.NOT_FOUND));
    }

    @Test
    public void create() {
        Meal mealToCreate = MealTestData.getNew();

        Meal created = service.create(mealToCreate, MealTestData.USER_ID);
        Integer newId = created.getId();
        mealToCreate.setId(newId);

        MealTestData.assertMatch(created, mealToCreate);
        MealTestData.assertMatch(service.get(newId, MealTestData.USER_ID), mealToCreate);
    }

    @Test
    public void createMealWithDuplicateDateTimeThrows() {
        Meal mealToCreate = MealTestData.getNew();
        mealToCreate.setDateTime(MealTestData.userMeal1.getDateTime());

        assertThrows(DataAccessException.class, () -> service.create(mealToCreate, MealTestData.USER_ID));
    }
}