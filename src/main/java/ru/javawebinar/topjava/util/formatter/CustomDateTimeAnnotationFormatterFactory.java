package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class CustomDateTimeAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {

    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[]{
                LocalDate.class, LocalTime.class}));
    }

    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<?> configureFormatterFrom(CustomDateTimeFormat annotation, Class<?> fieldType) {
        if (fieldType.equals(LocalDate.class)) {
            return new LocalDateFormatter();
        } else if (fieldType.equals(LocalTime.class)) {
            return new LocalTimeFormatter();
        }

        return null;
    }
}