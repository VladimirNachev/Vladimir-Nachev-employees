package com.example.employees.models;

import java.time.LocalDate;
import java.util.HashMap;

public class Projects {
//    private Map<Long, DateRanges> projectIdToDateRanges = new HashMap<>();


    public Projects() {
    }

    public void addProject(long projectId, LocalDate dateFrom, LocalDate dateTo) {
//        if (projectIdToDateRanges.containsKey(projectId)) {
//            projectIdToDateRanges.get(projectId).addRange(dateFrom, dateTo);
//            return;
//        }
//
//        projectIdToDateRanges.put(projectId, new DateRanges(dateFrom, dateTo));
    }

    public int calculateCommonWorkingDays(Projects otherEmployeeProjects) {
        return 0; // Fake 0 for now here.
    }
}
