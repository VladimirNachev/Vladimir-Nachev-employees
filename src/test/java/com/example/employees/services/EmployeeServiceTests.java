package com.example.employees.services;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.exceptions.EmptyCsvFileException;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeServiceTests {
    @Autowired
    @Qualifier("brute-force-approach")
    private EmployeeService employeeService;

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenEmployeesWorkRecordsGivenThenReturnCommonWorkingDays() {
//        143,12,2013-11-01,2014-01-05
//        218,10,2012-05-16,NULL
//        143,10,2009-01-01,2011-04-27
//        218,12,2014-01-02,2014-01-10
//        123,1,2023-05-05,2023/05/12
//        234,1,2023-05-01,2023.05.05
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 12, LocalDate.parse("2013-11-01"), LocalDate.parse("2014-01-05")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2012-05-16"), LocalDate.now()),
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 12, LocalDate.parse("2014-01-02"), LocalDate.parse("2014-01-10")),
                new EmployeeWorkRecord(123, 1, LocalDate.parse("2023-05-05"), LocalDate.parse("2023-05-12")),
                new EmployeeWorkRecord(234, 1, LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-05"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(employeesIdsAreExpected(employeesPairDTO, 143, 218));
        assertEquals(4, employeesPairDTO.getCommonWorkingDaysCount());
    }

    private boolean employeesIdsAreExpected(
            EmployeesPairDTO employeesPairDTO,
            long expectedFirstEmployeeId,
            long expectedSecondEmployeeId
    ) {
        return (employeesPairDTO.getFirstEmployeeId() == expectedFirstEmployeeId &&
                employeesPairDTO.getSecondEmployeeId() == expectedSecondEmployeeId) ||
                (employeesPairDTO.getFirstEmployeeId() == expectedSecondEmployeeId &&
                        employeesPairDTO.getSecondEmployeeId() == expectedFirstEmployeeId);
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenNoDateRangeOverlapBecauseOfDifferentProjectsThenReturnZeroWorkingDays() {
//        143,12,2013-11-01,2014-01-05
//        218,10,2012-05-16,NULL
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 12, LocalDate.parse("2013-11-01"), LocalDate.parse("2014-01-05")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2012-05-16"), LocalDate.now())
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(0, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenEmptyListWithEmployeesWorkRecordsGivenThenThrowException() {
        assertThrows(
                EmptyCsvFileException.class,
                () -> employeeService.findLongestWorkingPairOfEmployees(Collections.emptyList())
        );
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenListWithOnlyOneEmployeeWorkRecordGivenThenThrowException() {
//        143,12,2013-11-01,2014-01-05
        List<EmployeeWorkRecord> employeesWorkRecords = List.of(
                new EmployeeWorkRecord(143, 12, LocalDate.parse("2013-11-01"), LocalDate.parse("2014-01-05"))
        );

        assertThrows(
                OnlyOneEmployeeAvailableException.class,
                () -> employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords)
        );
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesLeftIntervalBeforeRightIntervalEntirely() {
        //      L      R
        //    [   ]  [   ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2014-01-02,2014-07-10 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2014-01-02"), LocalDate.parse("2014-07-10"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(0, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesLeftEndOfRightIntervalSameAsRightEndOfLeftInterval() {
        //      L   R
        //        [
        //    [   ]   ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2011-04-27,2014-07-10 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2011-04-27"), LocalDate.parse("2014-07-10"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(1, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesLeftEndOfRightIntervalInsideLeftInterval() {
        //      L    R
        //    [  [ ]    ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2011-04-25,2014-07-10 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2011-04-25"), LocalDate.parse("2014-07-10"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(3, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesLeftEndOfRightIntervalSameAsLeftEndOfLeftInterval() {
        //      L   R
        //    [
        //    [   ]    ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2009-01-01,2014-07-10 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2014-07-10"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(847, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesLeftEndOfRightIntervalBeforeLeftEndOfLeftInterval() {
        //      R   L   R
        //    [   [   ]    ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2008-12-31,2014-07-10 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2008-12-31"), LocalDate.parse("2014-07-10"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(847, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesRightEndOfRightIntervalSameAsRightEndOfLeftInterval() {
        //      R   L
        //            ]
        //    [   [   ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2008-12-31,2011-04-27 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2008-12-31"), LocalDate.parse("2011-04-27"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(847, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesRightEndOfRightIntervalInsideLeftInterval() {
        //      R   L
        //    [   [ ] ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2008-12-31,2011-04-26 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2008-12-31"), LocalDate.parse("2011-04-26"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(846, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesRightEndOfRightIntervalSameAsLeftEndOfLeftInterval() {
        //      R   L
        //        ]
        //    [   [   ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2008-12-31,2009-01-01 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2008-12-31"), LocalDate.parse("2009-01-01"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(1, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesRightIntervalBeforeLeftIntervalEntirely() {
        //     R      L
        //   [   ]  [   ]
        // <----------------> Ox
        // 218,10,2014-01-02,2014-07-10 L
        // 143,10,2009-01-01,2011-04-27 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2014-01-02"), LocalDate.parse("2014-07-10")),
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(0, employeesPairDTO.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesRightIntervalInsideLeftInterval() {
        //      L   R   L
        //    [   [   ]   ]
        // <----------------> Ox
        // 143,10,2009-01-01,2011-04-27 L
        // 218,10,2009-01-02,2011-04-26 R
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2009-01-02"), LocalDate.parse("2011-04-26"))
        );

        EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertEquals(845, employeesPairDTO.getCommonWorkingDaysCount());
    }

    // TODO: Add tests for the remaining cases.
}
