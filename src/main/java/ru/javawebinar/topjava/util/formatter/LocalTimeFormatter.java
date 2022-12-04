package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    private DateTimeFormatter pattern = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if(Objects.isNull(text) || text.isEmpty()){
            return null;
        }

        return LocalTime.parse(text, pattern);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(pattern);
    }
}
