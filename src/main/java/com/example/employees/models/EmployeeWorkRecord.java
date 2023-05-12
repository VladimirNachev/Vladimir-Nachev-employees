package com.example.employees.models;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.processor.ConvertWordNullToNull;
import com.opencsv.bean.processor.PreAssignmentProcessor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EmployeeWorkRecord {
    @NotNull
    @CsvBindByPosition(position = 0, required = true)
    private long employeeId;

    @NotNull
    @CsvBindByPosition(position = 1, required = true)
    private long projectId;

    @NotNull
    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByPosition(position = 2, required = true)
    private LocalDate dateFrom;

    @Nullable
    @CsvDate(value = "yyyy-MM-dd")
    @PreAssignmentProcessor(processor = ConvertWordNullToNull.class)
    @CsvBindByPosition(position = 3, required = true)
    private LocalDate dateTo;

    public EmployeeWorkRecord() {
    }

    public EmployeeWorkRecord(long employeeId, long projectId, LocalDate dateFrom, @Nullable LocalDate dateTo) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public long getProjectId() {
        return projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    @Nullable
    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(@Nullable LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
