package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    private Employee employee0;
    private static final String URI_MAIN_PATH = "/api/v1/employees";
    private static final String URI_FIND_BY_NAME = "?lastName=";

    @BeforeEach
    void prepareAndSaveInstancesToDatabase() {
        // Save an employee to the database before each test
        employee0 = new Employee("firstName", "LastName", "test@test.pl",
                Department.DEPARTMENT3, new Salary(1000.00));

        employeeRepository.save(employee0);
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
                .uri(URI_MAIN_PATH + "/" + employee0.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTOResponse.class)
                .consumeWith(response -> {
                    EmployeeDTOResponse actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);

                    assertEquals(employee0.getId(), actualResponse.getId());
                    assertEquals(employee0.getFirstName(), actualResponse.getFirstName());
                    assertEquals(employee0.getLastName(), actualResponse.getLastName());
                    assertEquals(employee0.getEmail(), actualResponse.getEmail());
                    assertEquals(employee0.getDepartment(), actualResponse.getDepartment());
                    assertEquals(employee0.getSalary(), actualResponse.getSalary());
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
                .uri(URI_MAIN_PATH + "/" + employee0.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class)
                .consumeWith(response -> {
                    ResponseDTO actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse, "Response body should not be null");

                    //check if it was deleted and does not exist in db anymore
                    Optional<Employee> shouldBeEmpty = employeeRepository.findById(employee0.getId());
                    assertTrue(shouldBeEmpty.isEmpty());

                    assertEquals(actualResponse.getMessage(), "Employee " + employee0.getFirstName() + " " +
                            employee0.getLastName() + " has been successfully deleted!");
                    assertEquals(actualResponse.getStatus(), HttpStatus.OK);
                });
    }

    @Nested
    class nestedClassForMultiEmployeesTests {

        private Employee employee1;
        private Employee employee2;
        private Employee employee3;
        private Employee employee4;
        private Iterable<Employee> employees;

        @BeforeEach
        void prepareEmployeesList() {
            employeeRepository.deleteAll();
            employee1 = new Employee("firstName1", "Klodnicki", "email1@email.com", Department.DEPARTMENT1,
                    new Salary(10000));
            employee2 = new Employee("firstName2", "lastName2", "email2@email.com", Department.DEPARTMENT1,
                    new Salary(8000));
            employee3 = new Employee("firstName3", "lastName3", "email3@email.com", Department.DEPARTMENT2,
                    new Salary(15000));
            employee4 = new Employee("firstName4", "Klodnicki", "email4@email.com", Department.DEPARTMENT3,
                    new Salary(22000));

            employees = employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4));
        }

        @Test
        public void findAll_ShouldFindAndReturnListEmployeeDTOResponse_WhenExist() {
            List<EmployeeDTOResponse> expectedList = new ArrayList<>();
            employees.forEach(employee -> {
                EmployeeDTOResponse employeeDTOResponse = modelMapper.map(employee, EmployeeDTOResponse.class);
                expectedList.add(employeeDTOResponse);
            });

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
                        assertThat(actualResponse).containsExactlyInAnyOrderElementsOf(expectedList);
                    });
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
                    .uri(URI_MAIN_PATH + URI_FIND_BY_NAME + lastName)
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
        public void findBySalaryRange_ShouldReturnListEmployeeDTOWithSalaryRange_WhenRangeIsGiven() {
            double minSalary = 5000;
            double maxSalary = 12000;
            List<EmployeeDTOResponse> expected = new ArrayList<>();
            List<Employee> employeeInRange = Arrays.asList(employee1, employee2);

            employeeInRange.forEach(employee -> {
                EmployeeDTOResponse employeeDTOResponse = modelMapper.map(employee, EmployeeDTOResponse.class);
                expected.add(employeeDTOResponse);
            });

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(URI_MAIN_PATH + "/salary")
                            .queryParam("from", minSalary)
                            .queryParam("to", maxSalary)
                            .build())
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
                    .uri(URI_MAIN_PATH + "?department=" + departmentName)
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

}