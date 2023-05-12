package com.example.employees.services;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import com.example.employees.models.EmployeesProjectTripple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.max;
import static org.apache.commons.lang3.ObjectUtils.min;

@Service
@Qualifier("brute-force-approach")
public class EmployeeServiceBruteForce implements EmployeeService {
    @Override
    public EmployeesPairDTO findLongestWorkingPairOfEmployees(List<EmployeeWorkRecord> employeesWorkRecords) {
        validateEmployeeWorkRecords(employeesWorkRecords);
        Map<EmployeesProjectTripple, Integer> employeesProjectTrippleToCommonWorkingDays = new HashMap<>();

        for (int i = 0; i < employeesWorkRecords.size() - 1; i++) {
            for (int j = i + 1; j < employeesWorkRecords.size(); j++) {
                EmployeeWorkRecord firstRow = employeesWorkRecords.get(i);
                EmployeeWorkRecord secondRow = employeesWorkRecords.get(j);

                if (areEmployeesDifferentAndWorkedOnSameProject(firstRow, secondRow)) {
                    EmployeesProjectTripple employeesProjectTripple = new EmployeesProjectTripple(
                            firstRow.getEmployeeId(), secondRow.getEmployeeId(), firstRow.getProjectId()
                    );
                    int commonWorkingDays = 0;
                    if (employeesProjectTrippleToCommonWorkingDays.containsKey(employeesProjectTripple)) {
                        commonWorkingDays = employeesProjectTrippleToCommonWorkingDays.get(employeesProjectTripple);
                    }
                    employeesProjectTrippleToCommonWorkingDays.put(
                            employeesProjectTripple,
                            commonWorkingDays + calculateCommonRangeDays(firstRow, secondRow)
                    );
                }
            }
        }

        Map.Entry<EmployeesProjectTripple, Integer> entry = Collections.max(employeesProjectTrippleToCommonWorkingDays.entrySet(),
                Comparator.comparingInt(Map.Entry::getValue));
        return buildEmployeesPairDTO(entry, employeesWorkRecords);
    }

    private EmployeesPairDTO buildEmployeesPairDTO(
            Map.Entry<EmployeesProjectTripple, Integer> entry,
            List<EmployeeWorkRecord> employeesWorkRecords
    ) {
        if (entry == null) {
            // If there is no entry since no two employees have worked together we just return two randomly chosen
            // employee ids (not necessarily different) and 0 common working days.
            return new EmployeesPairDTO(
                    employeesWorkRecords.get(0).getEmployeeId(),
                    employeesWorkRecords.get(1).getEmployeeId(),
                    0
            );
        }
        return new EmployeesPairDTO(
                entry.getKey().getFirstEmployeeId(),
                entry.getKey().getSecondEmployeeId(),
                entry.getValue()
        );
    }

    private boolean areEmployeesDifferentAndWorkedOnSameProject(EmployeeWorkRecord firstRow, EmployeeWorkRecord secondRow) {
        return firstRow.getEmployeeId() != secondRow.getEmployeeId() &&
                firstRow.getProjectId() == secondRow.getProjectId();
    }

    private int calculateCommonRangeDays(EmployeeWorkRecord firstRow, EmployeeWorkRecord secondRow) {
        if (secondRow.getDateFrom().isAfter(firstRow.getDateTo()) ||
                firstRow.getDateFrom().isAfter(secondRow.getDateTo())) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(
                max(firstRow.getDateFrom(), secondRow.getDateFrom()),
                min(firstRow.getDateTo(), secondRow.getDateTo())
        ) + 1;
    }

    private void validateEmployeeWorkRecords(List<EmployeeWorkRecord> employeeWorkRecords) {
        // TODO: Improve validation (for example, check if dateFrom <= dateTo for every record)
        if (employeeWorkRecords.size() == 1) {
            throw new OnlyOneEmployeeAvailableException();
        }
    }
}
