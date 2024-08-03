package org.klodnicki.rest.controller;

import lombok.AllArgsConstructor;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.service.AttendanceTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/attendance")
public class AttendanceTrackingController {

    private final AttendanceTrackingService attendanceTrackingService;

    // Endpoint for scanning badge in system A
    @PutMapping("/systemA/scan/{badgeNumber}")
    public ResponseEntity<String> scanBadgeSystemA(@RequestBody BadgeSystemA_DTO badgeSystemADto, @PathVariable String badgeNumber) {

        try {
            attendanceTrackingService.scanBadgeSystemA(badgeSystemADto, badgeNumber);
            return ResponseEntity.ok("Badge scanned successfully for System A");
        } catch (NotFoundInDatabaseException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint for scanning badge in system B
    @PostMapping("/systemB/scan")
    public ResponseEntity<String> scanBadgeSystemB(@RequestBody BadgeSystemB_DTO badgeSystemBDto) {

        try {
            attendanceTrackingService.scanBadgeSystemB(badgeSystemBDto);
            return ResponseEntity.ok("Badge scanned successfully for System B");
        } catch (NotFoundInDatabaseException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint for assigning badge to Employee in system A
    @PostMapping("/systemA/assign/{employeeId}")
    public ResponseEntity<String> assignBadgeToEmployeeSystemA(@RequestBody BadgeSystemA_DTO badgeSystemADto, @PathVariable Long employeeId) {
        try {
            attendanceTrackingService.assignBadgeToEmployeeSystemA(badgeSystemADto, employeeId);
            return ResponseEntity.ok("Badge assigned successfully for System A");
        } catch (NotFoundInDatabaseException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    //Endpoint for assigning badge to Employee in system B
    @PostMapping("/systemB/assign/{employeeId}")
    public ResponseEntity<String> assignBadgeToEmployeeSystemB(@RequestBody BadgeSystemB_DTO badgeSystemBDto, @PathVariable Long employeeId) {

        try {
            attendanceTrackingService.assignBadgeToEmployeeSystemB(badgeSystemBDto, employeeId);
            return ResponseEntity.ok("Badge assigned successfully for System B");
        } catch (NotFoundInDatabaseException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
