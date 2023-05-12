package com.example.employees.models;

import java.time.LocalDate;

public class Employee {
    private final long id;
    private final Projects projects = new Projects();

    public Employee(EmployeeWorkRecord employeeWorkRecord) {
        id = employeeWorkRecord.getEmployeeId();
        projects.addProject(
                employeeWorkRecord.getProjectId(),
                employeeWorkRecord.getDateFrom(),
                employeeWorkRecord.getDateTo()
        );
    }

    public long getId() {
        return id;
    }

    public Projects getProjects() {
        return projects;
    }

    public void addProject(long projectId, LocalDate dateFrom, LocalDate dateTo) {
        projects.addProject(projectId, dateFrom, dateTo);
    }

    public int calculateCommonWorkingDays(Employee otherEmployee) {
        Projects otherEmployeeProjects = otherEmployee.getProjects();
        return projects.calculateCommonWorkingDays(otherEmployeeProjects);
    }
}
