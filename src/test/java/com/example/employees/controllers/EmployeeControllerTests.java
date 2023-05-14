package com.example.employees.controllers;

import com.example.employees.TestUtils;
import com.example.employees.dtos.EmployeesPairDTO;
import com.example.employees.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTests {
    @SpyBean
    private final EmployeeService employeeService;

    private final TestRestTemplate testRestTemplate;

    private final TestUtils testUtils = new TestUtils();

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    public EmployeeControllerTests(
            @Qualifier("brute-force-approach") EmployeeService employeeService,
            TestRestTemplate testRestTemplate
    ) {
        this.employeeService = employeeService;
        this.testRestTemplate = testRestTemplate;
    }

    @Test
    public void testFindLongestWorkingTogetherPairOfEmployeesEndpointWillSuccessfullyExecute() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fileName", getTestFile("src/test/resources/employee-records.csv"));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = String.format("http://localhost:%s/upload-csv-file", randomServerPort);

        ResponseEntity<EmployeesPairDTO> response =
                testRestTemplate.postForEntity(url, requestEntity, EmployeesPairDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(testUtils.employeesIdsAreExpected(response.getBody().getEmployeesPair(), 143, 218));
        assertEquals(4, response.getBody().getEmployeesPair().getCommonWorkingDaysCount());
    }

    private FileSystemResource getTestFile(String relativeFilePath) {
        return new FileSystemResource(relativeFilePath);
    }

    @Test
    public void testFindLongestWorkingTogetherPairOfEmployeesEndpointWhenEmptyCsvPassedThenReturnBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fileName", getTestFile("src/test/resources/empty-employee-records.csv"));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = String.format("http://localhost:%s/upload-csv-file", randomServerPort);

        ResponseEntity<EmployeesPairDTO> response =
                testRestTemplate.postForEntity(url, requestEntity, EmployeesPairDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("You have passed an empty csv file !", response.getBody().getError());
    }

    @Test
    public void testFindLongestWorkingTogetherPairOfEmployeesEndpointWhenCsvFileWithOnlyOneRecordPassedThenReturnBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fileName", getTestFile("src/test/resources/only-one-row-employee-records.csv"));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = String.format("http://localhost:%s/upload-csv-file", randomServerPort);

        ResponseEntity<EmployeesPairDTO> response =
                testRestTemplate.postForEntity(url, requestEntity, EmployeesPairDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Only one employee available in the given csv file !", response.getBody().getError());
    }

    @Test
    public void testFindLongestWorkingTogetherPairOfEmployeesEndpointWhenUnexpectedErrorOccursThenReturnInternalServerError() {
        doThrow(new RuntimeException()).when(employeeService).findLongestWorkingPairOfEmployees(any());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fileName", getTestFile("src/test/resources/employee-records.csv"));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = String.format("http://localhost:%s/upload-csv-file", randomServerPort);

        ResponseEntity<EmployeesPairDTO> response =
                testRestTemplate.postForEntity(url, requestEntity, EmployeesPairDTO.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error while calculating longest working together pair of employees !", response.getBody().getError());
    }
}
