package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalDateFormatter implements Formatter<LocalDate> {

    private DateTimeFormatter pattern = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if(Objects.isNull(text) || text.isEmpty()){
            return null;
        }

        return LocalDate.parse(text, pattern);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.format(pattern);
    }
}
