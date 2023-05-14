package com.example.employees.controllers;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.exceptions.EmptyCsvFileException;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import com.example.employees.models.EmployeesPair;
import com.example.employees.services.EmployeeService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(@Qualifier("brute-force-approach") EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(path = "/upload-csv-file", consumes = "multipart/form-data")
    public ResponseEntity<?> findLongestWorkingTogetherPairOfEmployees(@RequestParam("fileName") MultipartFile file) {
        if (file == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new EmployeesPairDTO("You have not passed a csv file !"));
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<EmployeeWorkRecord> employeesWorkRecords = buildEmployeeWorkRecords(reader);
            EmployeesPair employeesPair = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);
            return ResponseEntity.status(HttpStatus.OK).body(new EmployeesPairDTO(employeesPair));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        if (e instanceof EmptyCsvFileException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new EmployeesPairDTO("You have passed an empty csv file !"));
        }
        if (e instanceof OnlyOneEmployeeAvailableException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new EmployeesPairDTO("Only one employee available in the given csv file !"));
        }
        logger.error(String.format("Error while calculating longest working together pair of employees ! exception=%s", e));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EmployeesPairDTO("Error while calculating longest working together pair of employees !"));
    }

    private List<EmployeeWorkRecord> buildEmployeeWorkRecords(Reader reader) {
        CsvToBean<EmployeeWorkRecord> csvToBean = new CsvToBeanBuilder<EmployeeWorkRecord>(reader)
                .withType(EmployeeWorkRecord.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        return csvToBean.parse();
    }
}
