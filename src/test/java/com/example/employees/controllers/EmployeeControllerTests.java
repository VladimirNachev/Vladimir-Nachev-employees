package com.example.employees.controllers;

import com.example.employees.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class EmployeeControllerTests {
    private final EmployeeService employeeService;

    private final MultipartBodyBuilder multipartBodyBuilder;

    @Autowired
    public EmployeeControllerTests(@Qualifier("brute-force-approach") EmployeeService employeeService) throws IOException {
        this.employeeService = employeeService;

//        URL url = Thread.currentThread().getContextClassLoader().getResource("employee-records.csv");
//        File csvFile = new File(url.getPath());

//        MultipartFile multipartFile = new MockMultipartFile("fileName", new FileInputStream(new File("/home/vladimir/Downloads/Java projects (educational)/sirma-interview-task/Vladimir-Nachev-employees/src/test/resources/employee-records.csv")));
        File csvFile = new File("employee-records.csv");
        multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("fileName",
                csvFile,
                MediaType.MULTIPART_FORM_DATA
//                new ClassPathResource("/home/vladimir/Downloads/Java projects (educational)/sirma-interview-task/Vladimir-Nachev-employees/src/test/resources/employee-records.csv"),
        );
    }

    @Test
    public void testFindLongestWorkingTogetherPairOfEmployeesEndpointWillSuccessfullyExecute() {
        EmployeeController employeeController = new EmployeeController(employeeService);
        WebTestClient webTestClient = WebTestClient.bindToController(employeeController).build();

        WebTestClient.ResponseSpec rawResponse = webTestClient.post()
                .uri("/upload-csv-file")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange();
//                .expectStatus().isOk()
//                .expectBody()
//                .consumeWith(response -> {
//                    assertNotNull(response.getResponseBody());
//                    assertEquals(1, 1); // TODO
//                });
        int a = 5;
    }
}
