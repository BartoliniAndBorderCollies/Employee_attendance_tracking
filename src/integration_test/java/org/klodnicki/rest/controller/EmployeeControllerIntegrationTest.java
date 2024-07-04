package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.DTO.Employee.EmployeeDTORequest;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties")
class EmployeeControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;
    private Employee savedEmployee;

    @BeforeEach
    void setUp() {
        // Save an employee to the database before each test
        savedEmployee = new Employee("firstName", "LastName", "test@test.pl",
                Department.DEPARTMENT3, new Salary(1000.00));
        savedEmployee = employeeRepository.save(savedEmployee);
    }

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

    @Test
    public void findById_ShouldFindAndReturnEmployeeDTO_WhenEmployeeIdIsGiven() {

        webTestClient.get()
                .uri("/api/v1/employees/find/" + savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    assertEquals(savedEmployee.getId(), actualResponse.getId());
                    assertEquals(savedEmployee.getFirstName(), actualResponse.getFirstName());
                    assertEquals(savedEmployee.getLastName(), actualResponse.getLastName());
                    assertEquals(savedEmployee.getEmail(), actualResponse.getEmail());
                    assertEquals(savedEmployee.getDepartment(), actualResponse.getDepartment());
                    assertEquals(savedEmployee.getSalary(), actualResponse.getSalary());
                });
    }

    @Test
    public void findAll_ShouldFindAndReturnListEmployeeDTOResponse_WhenExist() {
        //declare, initialize the list with savedEmployee converted straight to EmployeeDTOResponse
        List<EmployeeDTOResponse> expectedList = Arrays.asList(modelMapper.map(savedEmployee, EmployeeDTOResponse.class));

        webTestClient.get()
                .uri("/api/v1/employees/findAll")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    List<EmployeeDTOResponse> actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse, "Response body should not be null");
                    assertFalse(actualResponse.isEmpty(), "Response body should not be empty");

                    // Assert that actualResponse contains exactly the same elements as expectedList
                    assertThat(actualResponse)
                            .usingRecursiveComparison()
                            .ignoringFields("id") // Ignore ID field for comparison
                            .isEqualTo(expectedList);
                });
    }

    @Test
    public void update_ShouldUpdateEmployeeOnDatabase_WhenEmployeeDTORequestAndIdAreGiven() {
        Employee employeeToBeUpdated = new Employee("firstName", "LastName", "update@update.pl",
                Department.DEPARTMENT3, new Salary(1000.00));
        employeeRepository.save(employeeToBeUpdated);

        EmployeeDTORequest employeeDTORequest = new EmployeeDTORequest("updatedFirstName", "updatedLastName",
                "updatedEmail@update.pl", Department.DEPARTMENT2, new Salary(50.00));

        webTestClient.put()
                .uri("/api/v1/employees/update/" + employeeToBeUpdated.getId())
                .bodyValue(employeeDTORequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    Optional<Employee> optionalEmployee = employeeRepository.findById(employeeToBeUpdated.getId());
                    optionalEmployee.ifPresentOrElse(employee ->
                    {
                        assertEquals(employeeDTORequest.getFirstName(), employee.getFirstName());
                        assertEquals(employeeDTORequest.getLastName(), employee.getLastName());
                        assertEquals(employeeDTORequest.getEmail(), employee.getEmail());
                        assertEquals(employeeDTORequest.getDepartment(), employee.getDepartment());
                        assertEquals(employeeDTORequest.getSalary(), employee.getSalary());

                    }, () -> {
                        throw new RuntimeException("Employee not found in database");
                    });
                });
    }

}