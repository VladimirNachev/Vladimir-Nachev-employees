package com.example.employees.models;

import java.time.LocalDate;
import java.util.Objects;

public class Project {
    private long id;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Project() {
    }

    public Project(long id, LocalDate dateFrom, LocalDate dateTo) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id && Objects.equals(dateFrom, project.dateFrom) && Objects.equals(dateTo, project.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateFrom, dateTo);
    }
}
