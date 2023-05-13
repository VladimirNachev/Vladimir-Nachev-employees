package com.example.employees.services;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.models.EmployeeWorkRecord;

import java.util.List;

public interface EmployeeService {
    EmployeesPairDTO findLongestWorkingPairOfEmployees(List<EmployeeWorkRecord> employeesWorkRecords);
}
