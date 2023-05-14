package com.example.employees.utils.csv.converters;


import com.opencsv.bean.AbstractBeanField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class LocalDateConverter extends AbstractBeanField<LocalDate, Long> {

    @Override
    protected Object convert(String date) {
        if (date == null) {
            return LocalDate.now();
        }

        List<String> dateFormats = Arrays.asList("yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd");
        List<String> matchingDateFormats = dateFormats.stream()
                .filter(dateFormat -> dateFormatMatches(dateFormat, date))
                .toList();

        if (matchingDateFormats.isEmpty()) {
            throw new RuntimeException(String.format("Could not parse date=%s from csv", date));
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(matchingDateFormats.get(0)));
    }

    private boolean dateFormatMatches(String dateFormat, String date) {
        try {
            new SimpleDateFormat(dateFormat).parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
