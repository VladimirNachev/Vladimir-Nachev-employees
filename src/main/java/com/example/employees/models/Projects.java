package com.example.employees.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Projects {
    private final Map<Long, DateRanges> projectIdToDateRanges = new HashMap<>();


    public Projects() {
    }

    public void addProject(long projectId, LocalDate dateFrom, LocalDate dateTo) {
        if (projectIdToDateRanges.containsKey(projectId)) {
            projectIdToDateRanges.get(projectId).addDateRange(dateFrom, dateTo);
            return;
        }

        projectIdToDateRanges.put(projectId, new DateRanges().addDateRange(dateFrom, dateTo));
    }

    public int calculateCommonWorkingDays(Projects otherEmployeeProjects) {
        // TODO: Finish this method.
        return 0; // Fake 0 for now here.
    }
}
