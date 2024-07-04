package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.DTO.Employee.EmployeeDTORequest;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties")
class EmployeeControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    public void cleanDatabase() {
        employeeRepository.deleteAll();
    }


    @Test
    public void create_ShouldAddEmployeeToDatabaseAndReturnEmployeeDTOResponse_WhenEmployeeDTORequestIsGiven() {

        EmployeeDTORequest employeeDTORequest = new EmployeeDTORequest("firstName", "lastName",
                "email@test.pl", Department.DEPARTMENT1, new Salary(100.00));

        webTestClient.post()
                .uri("/api/v1/employees/create")
                .bodyValue(employeeDTORequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    // Check if the employee was really created and added to the database.
                    // If not, throw a custom NotFoundInDatabaseException.
                    // Since NotFoundInDatabaseException is a checked exception and it is not declared
                    // in the method signature with a throws clause, it must be caught within the method.
                    Optional<Employee> optionalEmployee = employeeRepository.findById(actualResponse.getId());
                    optionalEmployee.ifPresentOrElse(employee -> {

                        assertEquals(employeeDTORequest.getFirstName(), employee.getFirstName());
                        assertEquals(employeeDTORequest.getLastName(), employee.getLastName());
                        assertEquals(employeeDTORequest.getEmail(), employee.getEmail());
                        assertEquals(employeeDTORequest.getDepartment(), employee.getDepartment());
                        assertEquals(employeeDTORequest.getSalary(), employee.getSalary());

                    }, () -> {
                        throw new RuntimeException(new NotFoundInDatabaseException(Employee.class));
                    });
                });
    }

}