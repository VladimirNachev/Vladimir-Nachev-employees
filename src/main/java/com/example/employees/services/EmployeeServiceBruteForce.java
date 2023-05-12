package com.example.employees.services;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.Employee;
import com.example.employees.models.EmployeeWorkRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("brute-force-approach")
public class EmployeeServiceBruteForce implements EmployeeService {
    @Override
    public EmployeesPairDTO findLongestWorkingPairOfEmployees(List<EmployeeWorkRecord> employeesWorkRecords) {
        validateEmployeeWorkRecords(employeesWorkRecords);

        List<Employee> employees = buildEmployees(employeesWorkRecords);
        long firstEmployeeId = employeesWorkRecords.get(0).getEmployeeId();
        long secondEmployeeId = employeesWorkRecords.get(1).getEmployeeId();
        int maximumWorkingDaysTogether = 0;
        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employees.size(); j++) {
                Employee firstEmployee = employees.get(i);
                Employee secondEmployee = employees.get(j);
                int commonWorkingDays = firstEmployee.calculateCommonWorkingDays(secondEmployee);
                if (commonWorkingDays > maximumWorkingDaysTogether) {
                    maximumWorkingDaysTogether = commonWorkingDays;
                    firstEmployeeId = firstEmployee.getId();
                    secondEmployeeId = secondEmployee.getId();
                }
            }
        }

        return new EmployeesPairDTO(firstEmployeeId, secondEmployeeId, maximumWorkingDaysTogether);
    }

    private void validateEmployeeWorkRecords(List<EmployeeWorkRecord> employeeWorkRecords) {
        // TODO: Improve validation (for example, check if dateFrom <= dateTo for every record)
        if (employeeWorkRecords.size() == 1) {
            throw new OnlyOneEmployeeAvailableException();
        }
    }

    private List<Employee> buildEmployees(List<EmployeeWorkRecord> employeeWorkRecords) {
        List<Employee> employees = new ArrayList<>();
        if (employeeWorkRecords.isEmpty()) {
            return employees;
        }

        Map<Long, Integer> employeeIdToIndexMapping = new HashMap<>();
        employeeWorkRecords.forEach(employeeWorkRecord -> {
            long employeeId = employeeWorkRecord.getEmployeeId();
            if (employeeIdToIndexMapping.containsKey(employeeId)) {
                int resultListIndex = employeeIdToIndexMapping.get(employeeId);
                employees.get(resultListIndex).addProject(
                        employeeWorkRecord.getProjectId(),
                        employeeWorkRecord.getDateFrom(),
                        employeeWorkRecord.getDateTo()
                );
            } else {
                employeeIdToIndexMapping.put(employeeId, employees.size());
                employees.add(new Employee(employeeWorkRecord));
            }
        });

        return employees;
    }
}
