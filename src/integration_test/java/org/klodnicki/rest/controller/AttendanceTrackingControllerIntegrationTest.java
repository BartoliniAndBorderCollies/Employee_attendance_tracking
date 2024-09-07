package org.klodnicki.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.model.Action;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Badge;
import org.klodnicki.model.entity.BadgeScanHistory;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.BadgeRepository;
import org.klodnicki.repository.BadgeScanHistoryRepository;
import org.klodnicki.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties")
@AutoConfigureWebTestClient
class AttendanceTrackingControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BadgeRepository badgeRepository;
    private Badge badge;
    private BadgeSystemA_DTO badgeSystemADto;
    private BadgeSystemB_DTO badgeSystemBDto;
    @Autowired
    private BadgeScanHistoryRepository badgeScanHistoryRepository;
    private Employee employee;
    @Autowired
    private EmployeeRepository employeeRepository;
    private static final String URI_MAIN_PATH = "/attendance";
    private LocalDateTime exactTime = LocalDateTime.of(2024, 9, 6, 10, 10);

    @BeforeEach
    public void prepareEnvironment() {
        employee = new Employee("first name", "last name", "email", Department.DEPARTMENT1, new Salary(10000),
                "birth place", LocalDate.of(1983, 5, 1), Gender.MALE, null, "123456789",
                "bank account number", "pesel or nip", LocalDate.of(2024, 8, 15),
                null);
        employeeRepository.save(employee);

        badge = new Badge();
        badge.setBadgeNumber("12345");
        badge.setEmployee(employee);
        badgeRepository.save(badge);

        badgeSystemADto = new BadgeSystemA_DTO("12345", "Location A", "Device A", null);

        badgeSystemBDto = new BadgeSystemB_DTO("12345", "Location B", "Device B",
                Action.CLOCK_OUT, exactTime, null);

    }

    @AfterEach
    public void cleanDatabase() {
        badgeScanHistoryRepository.deleteAll();
        badgeRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void scanBadgeSystemA_ShouldSetVariablesToBadgeAndSaveItInRepoAndReturnResponseEntity_WhenScannedWithDevice() {

        webTestClient.put()
                .uri(URI_MAIN_PATH + "/systemA/scan/" + badge.getBadgeNumber())
                .bodyValue(badgeSystemADto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);
                    assertEquals("Badge scanned successfully for System A", actualResponse);

                    //Check if exactly one entry is in repository
                    long badgeScanHistoryCount = badgeScanHistoryRepository.count();
                    assertEquals(1, badgeScanHistoryCount);

                    Optional<BadgeScanHistory> badgeScanHistory = badgeScanHistoryRepository.findByBadge(badge);
                    assertTrue(badgeScanHistory.isPresent(), "Badge history not found in database");
                    BadgeScanHistory existingBadgeScanHistory = badgeScanHistory.get();

                    assertEquals(existingBadgeScanHistory.getBadge(), badge);
                    assertEquals(existingBadgeScanHistory.getLocation(), badgeSystemADto.getLocation());
                    assertEquals(existingBadgeScanHistory.getDeviceName(), badgeSystemADto.getDeviceName());
                    assertEquals(existingBadgeScanHistory.getEmployee(), badge.getEmployee());
                });
    }

    @Test
    public void scanBadgeSystemB_ShouldSetVariablesToBadgeAndSaveItInRepoAndReturnResponseEntity_WhenScannedWithDevice() {

        webTestClient.put()
                .uri(URI_MAIN_PATH + "/systemB/scan/" + badge.getBadgeNumber())
                .bodyValue(badgeSystemBDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String actualResponse = response.getResponseBody();
                    assertNotNull(actualResponse);
                    assertEquals("Badge scanned successfully for System B", actualResponse);

                    long badgeScanHistoryCount = badgeScanHistoryRepository.count();
                    assertEquals(1, badgeScanHistoryCount);

                    Optional<BadgeScanHistory> badgeScanHistory = badgeScanHistoryRepository.findByBadge(badge);
                    assertTrue(badgeScanHistory.isPresent(), "Badge history not found in database");
                    BadgeScanHistory existingBadgeHistory = badgeScanHistory.get();
                    existingBadgeHistory.setTimeStamp(exactTime);

                    assertEquals(existingBadgeHistory.getBadge(), badge);
                    assertEquals(existingBadgeHistory.getLocation(), badgeSystemBDto.getLocation());
                    assertEquals(existingBadgeHistory.getDeviceName(), badgeSystemBDto.getDeviceName());
                    assertEquals(existingBadgeHistory.getEmployee(), badge.getEmployee());
                    assertEquals(existingBadgeHistory.getAction(), badgeSystemBDto.getAction());
                    assertEquals(existingBadgeHistory.getTimeStamp(), exactTime);
                });


    }
}