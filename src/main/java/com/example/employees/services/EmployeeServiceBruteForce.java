package com.example.employees.services;

import com.example.employees.exceptions.EmptyCsvFileException;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import com.example.employees.models.EmployeesPair;
import com.example.employees.models.EmployeesProjectTripple;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.max;
import static org.apache.commons.lang3.ObjectUtils.min;

// TODO: Think of better names for the service class, qualifier annotation, etc.
@Service
@Qualifier("brute-force-approach")
public class EmployeeServiceBruteForce implements EmployeeService {

    @Override
    public EmployeesPair findLongestWorkingPairOfEmployees(List<EmployeeWorkRecord> employeesWorkRecords) {
        validateEmployeesWorkRecords(employeesWorkRecords);

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


        return buildEmployeesPair(employeesProjectTrippleToCommonWorkingDays, employeesWorkRecords);
    }

    // TODO: Start using Optional class for better null values handling in the whole project.
    private EmployeesPair buildEmployeesPair(
            Map<EmployeesProjectTripple, Integer> employeesProjectTrippleToCommonWorkingDays,
            List<EmployeeWorkRecord> employeesWorkRecords
    ) {
        if (employeesProjectTrippleToCommonWorkingDays.isEmpty()) {
            // If there is no entry since no two employees have worked together we just return two randomly chosen
            // employee ids (not necessarily different) and 0 common working days.
            return new EmployeesPair(
                    employeesWorkRecords.get(0).getEmployeeId(),
                    employeesWorkRecords.get(1).getEmployeeId(),
                    0
            );
        }
        Map<Pair<Long, Long>, Integer> employeePairsToCommonWorkingDays =
                buildEmployeePairsToCommonWorkingDays(employeesProjectTrippleToCommonWorkingDays);

        Map.Entry<Pair<Long, Long>, Integer> longestWorkingEmployeePair =
                employeePairsToCommonWorkingDays.entrySet().stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .orElseThrow();

        return new EmployeesPair(
                longestWorkingEmployeePair.getKey().getLeft(),
                longestWorkingEmployeePair.getKey().getRight(),
                longestWorkingEmployeePair.getValue()
        );
    }

    private Map<Pair<Long, Long>, Integer> buildEmployeePairsToCommonWorkingDays(
            Map<EmployeesProjectTripple, Integer> employeesProjectTrippleToCommonWorkingDays
    ) {
        return employeesProjectTrippleToCommonWorkingDays.entrySet().stream()
                .map(entry -> new EmployeesPair(
                        entry.getKey().getFirstEmployeeId(),
                        entry.getKey().getSecondEmployeeId(),
                        entry.getValue()))
                .collect(Collectors.toMap(
                        (entry) -> Pair.of(entry.getFirstEmployeeId(), entry.getSecondEmployeeId()),
                        EmployeesPair::getCommonWorkingDaysCount,
                        Integer::sum));
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

    private void validateEmployeesWorkRecords(List<EmployeeWorkRecord> employeesWorkRecords) {
        // TODO: Improve validation (for example, check if dateFrom <= dateTo for every record)
        if (employeesWorkRecords.isEmpty()) {
            throw new EmptyCsvFileException();
        }
        if (employeesWorkRecords.size() == 1) {
            throw new OnlyOneEmployeeAvailableException();
        }
    }
}
