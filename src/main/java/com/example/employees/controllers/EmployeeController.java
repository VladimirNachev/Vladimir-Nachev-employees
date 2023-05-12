package com.example.employees.controllers;

import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.exceptions.OnlyOneEmployeeAvailableException;
import com.example.employees.models.EmployeeWorkRecord;
import com.example.employees.services.EmployeeService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
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
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(@Qualifier("brute-force-approach") EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/upload-csv-file")
    public ResponseEntity<?> uploadCsvFileWithEmployees(@RequestParam("fileName") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have passed an empty csv file !");
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<EmployeeWorkRecord> employeesWorkRecords = buildEmployeeWorkRecords(reader);
            EmployeesPairDTO employeesPairDTO = employeeService.findLongestWorkingPairOfEmployees(employeesWorkRecords);
            return ResponseEntity.status(HttpStatus.OK).body(employeesPairDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        if (e instanceof OnlyOneEmployeeAvailableException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only one employee available in the given csv file !");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading CSV file " +
                "with employees work records !");
    }

    private List<EmployeeWorkRecord> buildEmployeeWorkRecords(Reader reader) {
        CsvToBean<EmployeeWorkRecord> csvToBean = new CsvToBeanBuilder<EmployeeWorkRecord>(reader)
                .withType(EmployeeWorkRecord.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        return csvToBean.parse().stream()
                .peek(employeeWorkRecord -> {
                    if (employeeWorkRecord.getDateTo() == null) {
                        employeeWorkRecord.setDateTo(LocalDate.now());
                    }
                })
                .collect(Collectors.toList());
    }
}
