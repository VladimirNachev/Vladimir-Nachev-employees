package com.example.employees.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DateRanges {
    private final List<DateRange> ranges = new ArrayList<>();

    public DateRanges() {
    }

    public DateRanges addDateRange(LocalDate dateFrom, LocalDate dateTo) {
        ranges.add(new DateRange(dateFrom, dateTo));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRanges that = (DateRanges) o;
        return Objects.equals(ranges, that.ranges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ranges);
    }
}
