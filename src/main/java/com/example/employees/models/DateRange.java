package com.example.employees.models;

import java.time.LocalDate;

public class DateRange {
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public DateRange() {
    }

    public DateRange(LocalDate dateFrom, LocalDate dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
