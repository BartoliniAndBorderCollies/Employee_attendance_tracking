package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties")
class SearchControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private Employee employee4;


    @BeforeEach
    public void prepareAndSaveInstancesToDatabase() {
        employee1 = new Employee("firstName1", "Klodnicki", "email1@email.com", Department.DEPARTMENT1,
                new Salary(10000));
        employee2 = new Employee("firstName2", "lastName2", "email2@email.com", Department.DEPARTMENT1,
                new Salary(8000));
        employee3 = new Employee("firstName3", "lastName3", "email3@email.com", Department.DEPARTMENT2,
                new Salary(15000));
        employee4 = new Employee("firstName4", "Klodnicki", "email4@email.com", Department.DEPARTMENT3,
                new Salary(22000));

        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4));
    }

    @AfterEach
    public void cleanDatabase() {
        employeeRepository.deleteAll();
    }

    @Test
    public void findByName_ShouldReturnListEmployeeDTOWithGivenLastName_WhenLastNameIsGiven() {
        String lastName = "Klodnicki";
        List<Employee> klodnickiEmployees = Arrays.asList(employee1, employee4);
        List<EmployeeDTOResponse> expected = new ArrayList<>();

        klodnickiEmployees.forEach(klodnicki -> {
            EmployeeDTOResponse employeeKlodnickiDTO = modelMapper.map(klodnicki, EmployeeDTOResponse.class);
            expected.add(employeeKlodnickiDTO);
        });

        webTestClient.get()
                .uri("/api/v1/search/employee/name?lastName=" + lastName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    List<EmployeeDTOResponse> actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse, "Actual response should not be null");
                    assertThat(actualResponse).containsExactlyInAnyOrderElementsOf(expected);
                });
    }

    @Test
    public void findByDepartment_ShouldReturnListEmployeeDTOWithAppropriateDepartment_WhenDepartmentIsGiven() {
        String departmentName = Department.DEPARTMENT1.name();
        List<EmployeeDTOResponse> expected = new ArrayList<>();
        List<Employee> department1Employees = Arrays.asList(employee1, employee2);

        department1Employees.forEach(employee -> {
            EmployeeDTOResponse employeeDTOresponse = modelMapper.map(employee, EmployeeDTOResponse.class);
            expected.add(employeeDTOresponse);
        });

        webTestClient.get()
                .uri("/api/v1/search/employee/department?department=" + departmentName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    List<EmployeeDTOResponse> actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse, "Actual response should not be null");
                    assertThat(actualResponse).containsExactlyInAnyOrderElementsOf(expected);
                });
    }

}