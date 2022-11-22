package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    private static final String USER_ID = "userId";
    private static final String MEALS = "meals";

    @PostMapping("/add")
    protected void addMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = getUserId(request);
        log.info("setMeal for user {}", userId);

        Meal meal = getMealFromRequest(request);

        service.create(meal, userId);
        response.sendRedirect(MEALS);
    }

    @PostMapping("/update")
    protected void updateMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = getUserId(request);
        log.info("updateMeal for user {}", userId);

        Meal meal = getMealFromRequest(request);

        service.update(meal, userId);
        response.sendRedirect(MEALS);
    }

    @GetMapping()
    protected void getMeal(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int userId = getUserId(request);
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("getMeal with id={} for user {}", id, userId);

        request.setAttribute(MEALS, service.get(id, userId));
        request.getRequestDispatcher("/meals").forward(request, response);
    }

    @GetMapping("/all")
    protected void getAllMeals(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int userId = getUserId(request);
        log.info("getAllMeals for user {}", userId);

        request.setAttribute(MEALS, service.getAll(userId));
        request.getRequestDispatcher("/meals").forward(request, response);
    }

    @GetMapping("/filtered")
    protected void getMealsBetweenDates(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int userId = getUserId(request);
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        log.info("getMealsBetweenDates for user {}, startDate={}, endDate={}", userId, startDate, endDate);

        request.setAttribute("meal", service.getBetweenInclusive(startDate, endDate, userId));
        request.getRequestDispatcher("/mealForm").forward(request, response);
    }

    @DeleteMapping()
    protected void deleteMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = getUserId(request);
        int id = getId(request);
        log.info("deleteMeal with id={} for user {}", id, userId);

        service.delete(id, userId);
        response.sendRedirect(MEALS);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private int getUserId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter(USER_ID));
        return Integer.parseInt(paramId);
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
