package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.formatter.CustomDateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

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

    @GetMapping("/filter/{startTime}/{endTime}")
    public List<MealTo> getBetween(@PathVariable("startTime") String startTimeString, @PathVariable("endTime") String endTimeString) {
        LocalDateTime startDateTime = LocalDateTime.parse(startTimeString, ISO_DATE_TIME_FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(endTimeString, ISO_DATE_TIME_FORMATTER);
        return super.getBetween(startDateTime.toLocalDate(), startDateTime.toLocalTime(), endDateTime.toLocalDate(), endDateTime.toLocalTime());
    }

    @GetMapping("/filter/v2/{start}/{end}")
    public List<MealTo> getBetweenDateOrTime(@PathVariable String start, @PathVariable String end) {
        try {
            LocalDate startDate = LocalDate.parse(start, ISO_DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(end, ISO_DATE_FORMATTER);
            return super.getBetween(startDate, null, endDate, null);
        } catch (DateTimeParseException dateEx) {
            try {
                LocalTime startTime = LocalTime.parse(start, ISO_TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(end, ISO_TIME_FORMATTER);
                return super.getBetween(null, startTime, null, endTime);
            } catch (DateTimeParseException timeEx) {
                throw new DateTimeParseException("Unable to parse passed timeframe: " + dateEx + "/n" + timeEx,
                        dateEx.getParsedString() + "/n" + timeEx.getParsedString(),
                        dateEx.getErrorIndex());
            }
        }

    }

    @GetMapping("/filter/v3")
    public List<MealTo> getBetweenDateOrTime(@RequestParam(value = "start_time", required = false) @CustomDateTimeFormat LocalTime startTime,
                                             @RequestParam(value = "start_date", required = false) @CustomDateTimeFormat LocalDate startDate,
                                             @RequestParam(value = "end_time", required = false) @CustomDateTimeFormat LocalTime endTime,
                                             @RequestParam(value = "end_date", required = false) @CustomDateTimeFormat LocalDate endDate) {
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