package com.example.employees.services;

import com.example.employees.models.EmployeesPair;
import com.example.employees.models.EmployeeWorkRecord;

import java.util.List;

public interface EmployeeService {
    EmployeesPair findLongestWorkingPairOfEmployees(List<EmployeeWorkRecord> employeesWorkRecords);
}
