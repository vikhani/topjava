package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.*;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL + "/" + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsTo));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL
                + "/filter/"
                + START_DATE_TIME.format(ISO_DATE_TIME_FORMATTER)
                + "/"
                + END_DATE_TIME.format(ISO_DATE_TIME_FORMATTER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsToFiltered));
    }

    @Test
    void getBetweenTime() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL
                + "/filter/v2/"
                + START_DATE_TIME.toLocalTime().format(ISO_TIME_FORMATTER)
                + "/"
                + END_DATE_TIME.toLocalTime().format(ISO_TIME_FORMATTER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsToFiltered));
    }

    @Test
    void getBetweenDate() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL
                + "/filter/v2/"
                + START_DATE_TIME.toLocalDate().format(ISO_DATE_FORMATTER)
                + "/"
                + END_DATE_TIME.toLocalDate().format(ISO_DATE_FORMATTER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsToFiltered));
    }

    @Test
    void getBetweenDateV3() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL
                + "/filter/v3?start_date="
                + START_DATE_TIME.toLocalDate().format(ISO_DATE_FORMATTER)
                + "&end_date="
                + END_DATE_TIME.toLocalDate().format(ISO_DATE_FORMATTER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsToFiltered));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(MealRestController.REST_URL + "/" + MEAL1_ID))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.getAll(SecurityUtil.authUserId()), meals.stream().filter(meal -> meal.getId() != MEAL1_ID).toList());
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.post(MealRestController.REST_URL + "/update/" + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void create() throws Exception {
        Meal created = MealTestData.getNew();
        perform(MockMvcRequestBuilders.post(MealRestController.REST_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(MEAL_MATCHER.contentJson(created));
    }
}