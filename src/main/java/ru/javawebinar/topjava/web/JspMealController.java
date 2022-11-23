package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
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

    private static final String MEALS = "meals";

    @PostMapping("/add")
    protected String add(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("setMeal for user {}", userId);

        Meal meal = getMealFromRequest(request);

        service.create(meal, userId);
        return MEALS;
    }

    @PostMapping("/update")
    protected String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("updateMeal for user {}", userId);

        Meal meal = getMealFromRequest(request);

        service.update(meal, userId);
        return MEALS;
    }

    @GetMapping()
    protected String get(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("getMeal with id={} for user {}", id, userId);

        model.addAttribute("meal", service.get(id, userId));
        return MEALS;
    }

    @GetMapping("/all")
    protected String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllMeals for user {}", userId);

        model.addAttribute(MEALS, service.getAll(userId));
        return MEALS;
    }

    @GetMapping("/filtered")
    protected String getBetweenDates(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        log.info("getMealsBetweenDates for user {}, startDate={}, endDate={}", userId, startDate, endDate);

        model.addAttribute(MEALS, service.getBetweenInclusive(startDate, endDate, userId));
        return "mealForm";
    }

    @DeleteMapping()
    protected String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("deleteMeal with id={} for user {}", id, userId);

        service.delete(id, userId);
        return MEALS;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
