package com.example.employees.services;

import com.example.employees.TestUtils;
import com.example.employees.exceptions.EmptyCsvFileException;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import com.example.employees.models.EmployeesPair;
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

    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenEmployeesWorkRecordsGivenThenReturnCommonWorkingDays() {
        // 143,12,2013-11-01,2014-01-05
        // 218,10,2012-05-16,NULL
        // 143,10,2009-01-01,2011-04-27
        // 218,12,2014-01-02,2014-01-10
        // 123,1,2023-05-05,2023/05/12
        // 234,1,2023-05-01,2023.05.05
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 12, LocalDate.parse("2013-11-01"), LocalDate.parse("2014-01-05")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2012-05-16"), LocalDate.now()),
                new EmployeeWorkRecord(143, 10, LocalDate.parse("2009-01-01"), LocalDate.parse("2011-04-27")),
                new EmployeeWorkRecord(218, 12, LocalDate.parse("2014-01-02"), LocalDate.parse("2014-01-10")),
                new EmployeeWorkRecord(123, 1, LocalDate.parse("2023-05-05"), LocalDate.parse("2023-05-12")),
                new EmployeeWorkRecord(234, 1, LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-05"))
        );

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(4, employeesPair.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenNoDateRangeOverlapBecauseOfDifferentProjectsThenReturnZeroWorkingDays() {
        // 143,12,2013-11-01,2014-01-05
        // 218,10,2012-05-16,NULL
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(143, 12, LocalDate.parse("2013-11-01"), LocalDate.parse("2014-01-05")),
                new EmployeeWorkRecord(218, 10, LocalDate.parse("2012-05-16"), LocalDate.now())
        );

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(0, employeesPair.getCommonWorkingDaysCount());
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
        // 143,12,2013-11-01,2014-01-05
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(0, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(1, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(3, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(847, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(847, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(847, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(846, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(1, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 218, 143));
        assertEquals(0, employeesPair.getCommonWorkingDaysCount());
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

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 143, 218));
        assertEquals(845, employeesPair.getCommonWorkingDaysCount());
    }

    @Test
    public void testFindLongestWorkingPairOfEmployeesWhenOnePairWorkingLongestTogetherButOnMultipleProjectsRatherThanOnOneProject() {
//        1,1,2023-01-01,2023-01-05
//        2,1,2023-01-02,2023-01-04
//        1,2,2023-02-01,2023-02-05
//        2,2,2023-02-02,2023-02-07
//        3,3,2023-01-01,2023-01-05
//        4,3,2023-01-01,2023-01-05
//        5,4,2023-01-01,2023-01-02
//        6,4,2023-01-01,2023-01-02
//        5,5,2023-02-01,2023-02-02
//        6,5,2023-02-02,2023-02-02
//        5,6,2023-03-01,2023-03-03
//        6,6,2023-03-01,2023-03-03
        List<EmployeeWorkRecord> employeesWorkRecords = Arrays.asList(
                new EmployeeWorkRecord(1, 1, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")),
                new EmployeeWorkRecord(2, 1, LocalDate.parse("2023-01-02"), LocalDate.parse("2023-01-04")),
                new EmployeeWorkRecord(1, 2, LocalDate.parse("2023-02-01"), LocalDate.parse("2023-02-05")),
                new EmployeeWorkRecord(2, 2, LocalDate.parse("2023-02-02"), LocalDate.parse("2023-02-07")),
                new EmployeeWorkRecord(3, 3, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")),
                new EmployeeWorkRecord(4, 3, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")),
                new EmployeeWorkRecord(5, 4, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-02")),
                new EmployeeWorkRecord(6, 4, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-02")),
                new EmployeeWorkRecord(5, 5, LocalDate.parse("2023-02-01"), LocalDate.parse("2023-02-02")),
                new EmployeeWorkRecord(6, 5, LocalDate.parse("2023-02-02"), LocalDate.parse("2023-02-02")),
                new EmployeeWorkRecord(5, 6, LocalDate.parse("2023-03-01"), LocalDate.parse("2023-03-03")),
                new EmployeeWorkRecord(6, 6, LocalDate.parse("2023-03-01"), LocalDate.parse("2023-03-03"))
        );

        EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);

        assertTrue(testUtils.employeesIdsAreExpected(employeesPair, 1, 2));
        assertEquals(7, employeesPair.getCommonWorkingDaysCount());
    }

    // TODO: Add tests for the remaining cases.
}
