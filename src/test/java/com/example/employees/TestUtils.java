package com.example.employees;

import com.example.employees.models.EmployeesPair;

public class TestUtils {
    public boolean employeesIdsAreExpected(
            EmployeesPair employeesPair,
            long expectedFirstEmployeeId,
            long expectedSecondEmployeeId
    ) {
        return (employeesPair.getFirstEmployeeId() == expectedFirstEmployeeId &&
                employeesPair.getSecondEmployeeId() == expectedSecondEmployeeId) ||
                (employeesPair.getFirstEmployeeId() == expectedSecondEmployeeId &&
                        employeesPair.getSecondEmployeeId() == expectedFirstEmployeeId);
    }
}
