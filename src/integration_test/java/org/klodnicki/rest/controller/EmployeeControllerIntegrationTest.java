package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.dto.employee.EmployeeDTORequest;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.dto.ResponseDTO;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties")
@AutoConfigureWebTestClient
class EmployeeControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;
    private Employee savedEmployee;
    private static final String URI_MAIN_PATH = "/api/v1/employees";

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
                .uri(URI_MAIN_PATH)
                .bodyValue(employeeDTORequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    // Check if the employee was really created and added to the database.
                    Optional<Employee> optionalEmployee = employeeRepository.findById(actualResponse.getId());
                    assertTrue(optionalEmployee.isPresent(), "Employee not found in database");

                    Employee employee = optionalEmployee.get();
                    assertEquals(employeeDTORequest.getFirstName(), employee.getFirstName());
                    assertEquals(employeeDTORequest.getLastName(), employee.getLastName());
                    assertEquals(employeeDTORequest.getEmail(), employee.getEmail());
                    assertEquals(employeeDTORequest.getDepartment(), employee.getDepartment());
                    assertEquals(employeeDTORequest.getSalary(), employee.getSalary());
                });
    }

    @Test
    public void findById_ShouldFindAndReturnEmployeeDTO_WhenEmployeeIdIsGiven() {

        webTestClient.get()
                .uri(URI_MAIN_PATH + "/" + savedEmployee.getId())
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
                .uri(URI_MAIN_PATH)
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
                .uri(URI_MAIN_PATH + "/" + employeeToBeUpdated.getId())
                .bodyValue(employeeDTORequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    Optional<Employee> optionalEmployee = employeeRepository.findById(employeeToBeUpdated.getId());
                    assertTrue(optionalEmployee.isPresent(), "Employee not found in database");

                    Employee employee = optionalEmployee.get();
                    assertEquals(employeeDTORequest.getFirstName(), employee.getFirstName());
                    assertEquals(employeeDTORequest.getLastName(), employee.getLastName());
                    assertEquals(employeeDTORequest.getEmail(), employee.getEmail());
                    assertEquals(employeeDTORequest.getDepartment(), employee.getDepartment());
                    assertEquals(employeeDTORequest.getSalary(), employee.getSalary());
                });
    }

    @Test
    public void deleteById_ShouldDeleteEmployee_WhenIdIsGiven() {

        webTestClient.delete()
                .uri(URI_MAIN_PATH + "/" + savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class)
                .consumeWith(response -> {
                    ResponseDTO actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse, "Response body should not be null");

                    //check if it was deleted and does not exist in db anymore
                    Optional<Employee> shouldBeEmpty = employeeRepository.findById(savedEmployee.getId());
                    assertTrue(shouldBeEmpty.isEmpty());

                    assertEquals(actualResponse.getMessage(), "Employee " + savedEmployee.getFirstName() + " " +
                            savedEmployee.getLastName() + " has been successfully deleted!");
                    assertEquals(actualResponse.getStatus(), HttpStatus.OK);
                });
    }

}