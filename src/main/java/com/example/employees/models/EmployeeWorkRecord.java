package com.example.employees.models;

import com.example.employees.utils.csv.converters.LocalDateConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.processor.ConvertWordNullToNull;
import com.opencsv.bean.processor.PreAssignmentProcessor;

import java.time.LocalDate;

public class EmployeeWorkRecord {
    @CsvBindByPosition(position = 0, required = true)
    private long employeeId;

    @CsvBindByPosition(position = 1, required = true)
    private long projectId;

    @CsvCustomBindByPosition(position = 2, required = true, converter = LocalDateConverter.class)
    private LocalDate dateFrom;

    @PreAssignmentProcessor(processor = ConvertWordNullToNull.class)
    @CsvCustomBindByPosition(position = 3, required = true, converter = LocalDateConverter.class)
    private LocalDate dateTo;

    public EmployeeWorkRecord() {
    }

    public EmployeeWorkRecord(long employeeId, long projectId, LocalDate dateFrom, LocalDate dateTo) {
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

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
