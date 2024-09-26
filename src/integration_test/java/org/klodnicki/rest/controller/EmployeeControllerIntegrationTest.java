package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.klodnicki.dto.employee.EmployeeDTORequest;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.dto.ResponseDTO;
import org.klodnicki.model.Address;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Badge;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.BadgeRepository;
import org.klodnicki.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private BadgeRepository badgeRepository;
    private Badge badge;

    @BeforeEach
    void prepareAndSaveInstancesToDatabase() {
        // Save an employee to the database before each test
        employee0 = new Employee("firstName", "LastName", "test@test.pl",
                Department.DEPARTMENT3, new Salary(1000.00), "Gdańsk", LocalDate.of(1991, 2, 21),
                Gender.MALE, new Address("test street", "test houseNumber", "11015", "test city", "test country"), "123456789", "11-04-0000-1111-2345-2345",
                "Pesel 123453423234", LocalDate.of(2024, 7, 10), null);

        employeeRepository.save(employee0);
    }

    @BeforeEach
    void prepareAndSaveBadge() {
        badge = new Badge(null, "badge 123", "Hall test", "device test name", null, LocalDateTime.now(), employee0);
        badgeRepository.save(badge);
    }

    @AfterEach
    public void cleanDatabase() {
        employeeRepository.deleteAll();
        badgeRepository.deleteAll();
    }

    @Test
    public void create_ShouldAddEmployeeToDatabaseAndReturnEmployeeDTOResponse_WhenEmployeeDTORequestIsGiven() {

        EmployeeDTORequest employeeDTORequest = new EmployeeDTORequest(
                "firstName",
                "lastName",
                "email@test.pl",
                Department.DEPARTMENT1,
                100.00,  // Flattened salary
                "Warsaw",
                "1999-01-01",  // rawBirthDate
                "2022-02-01",  // rawDateOfEmployment
                LocalDate.of(1999, 1, 1),  // birthDate
                LocalDate.of(2022, 2, 1),  // dateOfEmployment
                Gender.FEMALE,
                "street",
                "house nr",
                "11-015",
                "City",
                "Norway",
                "123telephone",
                "123bankAccount",
                "StringOrPesel",
                null  // Badge is null
        );


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
                    assertEquals(employeeDTORequest.getSalaryAmount(), employee.getSalary().getAmount(), 0.01); // Compare salary
                    assertEquals(employeeDTORequest.getBirthPlace(), employee.getBirthPlace());

                    // Parse rawBirthDate and rawDateOfEmployment in the service and check parsed LocalDate
                    LocalDate parsedBirthDate = LocalDate.parse(employeeDTORequest.getRawBirthDate());
                    LocalDate parsedDateOfEmployment = LocalDate.parse(employeeDTORequest.getRawDateOfEmployment());
                    assertEquals(parsedBirthDate, employee.getBirthDate());
                    assertEquals(parsedDateOfEmployment, employee.getDateOfEmployment());

                    assertEquals(employeeDTORequest.getGender(), employee.getGender());
                    assertEquals(employeeDTORequest.getStreet(), employee.getAddress().getStreet());
                    assertEquals(employeeDTORequest.getHouseNumber(), employee.getAddress().getHouseNumber());
                    assertEquals(employeeDTORequest.getPostalCode(), employee.getAddress().getPostalCode());
                    assertEquals(employeeDTORequest.getCity(), employee.getAddress().getCity());
                    assertEquals(employeeDTORequest.getCountry(), employee.getAddress().getCountry());
                    assertEquals(employeeDTORequest.getTelephoneNumber(), employee.getTelephoneNumber());
                    assertEquals(employeeDTORequest.getBankAccountNumber(), employee.getBankAccountNumber());
                    assertEquals(employeeDTORequest.getPeselOrNip(), employee.getPeselOrNip());
                    assertNull(employee.getBadge());
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
                    assertEquals(employee0.getBirthPlace(), actualResponse.getBirthPlace());
                    assertEquals(employee0.getGender(), actualResponse.getGender());
                    assertEquals(employee0.getAddress(), actualResponse.getAddress());
                    assertEquals(employee0.getTelephoneNumber(), actualResponse.getTelephoneNumber());
                    assertEquals(employee0.getBankAccountNumber(), actualResponse.getBankAccountNumber());
                    assertEquals(employee0.getPeselOrNip(), actualResponse.getPeselOrNip());
                    assertEquals(employee0.getDateOfEmployment(), actualResponse.getDateOfEmployment());
                    assertNull(employee0.getBadge());
                });
    }

    @Test
    public void update_ShouldUpdateEmployeeOnDatabase_WhenEmployeeDTORequestAndIdAreGiven() {
        Employee employeeToBeUpdated = new Employee("firstName", "LastName", "test2@test.pl",
                Department.DEPARTMENT3, new Salary(1000.00), "Warszawa", LocalDate.of(1900, 1, 1),
                Gender.FEMALE, new Address(), "11111111111", "bank account number",
                "NIP 789-111-111-12", LocalDate.of(2024, 7, 19), null);
        employeeRepository.save(employeeToBeUpdated);

        EmployeeDTORequest employeeDTORequest = new EmployeeDTORequest("updatedFirstName", "updatedLastName",
                "updatedEmail@update.pl", Department.DEPARTMENT2, new Salary(50.00), "Updated birth place",
                LocalDate.of(1983, 9, 12), Gender.FEMALE, new Address("Street", "12",
                "11-015", "Poznan", "Poland"), "updated phone","updated bank account",
                "updated pesel", LocalDate.of(1922, 3, 3), null);

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
                    assertEquals(employeeDTORequest.getBirthDate(), employee.getBirthDate());
                    assertEquals(employeeDTORequest.getBirthPlace(), employee.getBirthPlace());
                    assertEquals(employeeDTORequest.getGender(), employee.getGender());
                    assertEquals(employeeDTORequest.getAddress(), employee.getAddress());
                    assertEquals(employeeDTORequest.getTelephoneNumber(), employee.getTelephoneNumber());
                    assertEquals(employeeDTORequest.getBankAccountNumber(), employee.getBankAccountNumber());
                    assertEquals(employeeDTORequest.getPeselOrNip(), employee.getPeselOrNip());
                    assertEquals(employeeDTORequest.getDateOfEmployment(), employee.getDateOfEmployment());
                    assertNull(employeeDTORequest.getBadge());

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

            employee1 = new Employee(
                    "John",
                    "Klodnicki",
                    "email1@email.com",
                    Department.DEPARTMENT1,
                    new Salary(10000.00),
                    "Milano",
                    LocalDate.of(1990, 5, 15),
                    Gender.MALE,
                    new Address("Main St", "123", "20100", "Milano", "Italy"),
                    "123456789",
                    "11-22-3333-4444-5555",
                    "Pesel 12345678901",
                    LocalDate.of(2015, 1, 10),
                    null
            );

            employee2 = new Employee(
                    "Alice",
                    "Smith",
                    "email2@email.com",
                    Department.DEPARTMENT1,
                    new Salary(8000.00),
                    "New York",
                    LocalDate.of(1985, 8, 22),
                    Gender.FEMALE,
                    new Address("Broadway", "456", "10001", "New York", "USA"),
                    "987654321",
                    "22-33-4444-5555-6666",
                    "Pesel 23456789012",
                    LocalDate.of(2018, 3, 5),
                    null
            );

            employee3 = new Employee(
                    "Michael",
                    "Johnson",
                    "email3@email.com",
                    Department.DEPARTMENT2,
                    new Salary(15000.00),
                    "Los Angeles",
                    LocalDate.of(1992, 12, 10),
                    Gender.MALE,
                    new Address("Sunset Blvd", "789", "90001", "Los Angeles", "USA"),
                    "1122334455",
                    "33-44-5555-6666-7777",
                    "Pesel 34567890123",
                    LocalDate.of(2020, 5, 20),
                    null
            );

            employee4 = new Employee(
                    "Emma",
                    "Klodnicki",
                    "email4@email.com",
                    Department.DEPARTMENT3,
                    new Salary(22000.00),
                    "Paris",
                    LocalDate.of(1988, 7, 18),
                    Gender.FEMALE,
                    new Address("Champs Elysees", "321", "75008", "Paris", "France"),
                    "5566778899",
                    "44-55-6666-7777-8888",
                    "Pesel 45678901234",
                    LocalDate.of(2017, 9, 25),
                    null
            );

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