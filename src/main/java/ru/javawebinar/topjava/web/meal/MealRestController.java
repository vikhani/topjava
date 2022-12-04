package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.formatter.CustomDateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam(value = "start_date", required = false) @CustomDateTimeFormat LocalDate startDate,
                                   @RequestParam(value = "start_time", required = false) @CustomDateTimeFormat LocalTime startTime,
                                   @RequestParam(value = "end_date", required = false) @CustomDateTimeFormat LocalDate endDate,
                                   @RequestParam(value = "end_time", required = false) @CustomDateTimeFormat LocalTime endTime) {
        boolean missingDateFrames = Objects.isNull(startDate) || Objects.isNull(endDate);
        boolean missingTimeFrames = Objects.isNull(startTime) || Objects.isNull(endTime);

        if(missingTimeFrames && missingDateFrames) {
            return Collections.emptyList();
        }

        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PostMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @Override
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }
}