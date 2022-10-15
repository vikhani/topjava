package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealRepository mealRepo = new MealRepository();
    private final UserRepository userRepo = new UserRepository();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        mealRepo.setMeals(MealsUtil.generateMeals());
        log.info("generated meals");
        userRepo.setCaloriesPerDay(2000);
        log.info("added calories restriction");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsWithExcess = MealsUtil.filteredByStreams(mealRepo.getMeals(),
                LocalTime.MIN,
                LocalTime.MAX,
                userRepo.getCaloriesPerDay());
        request.setAttribute("meals", mealsWithExcess);

        log.debug("redirect to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
