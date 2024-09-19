package org.klodnicki.util;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    protected LocalDate convert(String value) {
        try {
            // Try parsing with the first format
            return LocalDate.parse(value, FORMATTER1);
        } catch (DateTimeParseException e) {
            // If it fails, try parsing with the second format
            try {
                return LocalDate.parse(value, FORMATTER2);
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("Error parsing date: " + value, ex);
            }
        }
    }
}
