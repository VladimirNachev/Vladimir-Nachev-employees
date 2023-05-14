package com.example.employees.dtos;

import com.example.employees.models.EmployeesPair;

// TODO: Enhance this class so that 'error' field is of some new type 'Error' and has more data inside it.
public class EmployeesPairDTO {
    private EmployeesPair employeesPair = null;
    private String error = null;

    public EmployeesPairDTO(EmployeesPair employeesPair) {
        this.employeesPair = employeesPair;
    }

    public EmployeesPairDTO(String error) {
        this.error = error;
    }

    public EmployeesPairDTO(EmployeesPair employeesPair, String error) {
        this.employeesPair = employeesPair;
        this.error = error;
    }

    public EmployeesPair getEmployeesPair() {
        return employeesPair;
    }

    public String getError() {
        return error;
    }
}
