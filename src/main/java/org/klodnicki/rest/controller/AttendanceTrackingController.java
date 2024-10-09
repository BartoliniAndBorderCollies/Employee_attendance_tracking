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
    public ResponseEntity<String> scanBadgeSystemA(@RequestBody BadgeSystemA_DTO badgeSystemADto, @PathVariable String badgeNumber)
            throws NotFoundInDatabaseException {

        attendanceTrackingService.scanBadgeSystemA(badgeSystemADto, badgeNumber);
        return ResponseEntity.ok("Badge scanned successfully for System A");
    }

    // Endpoint for scanning badge in system B
    @PutMapping("/systemB/scan/{badgeNumber}")
    public ResponseEntity<String> scanBadgeSystemB(@RequestBody BadgeSystemB_DTO badgeSystemBDto, @PathVariable String badgeNumber)
            throws NotFoundInDatabaseException {

        attendanceTrackingService.scanBadgeSystemB(badgeSystemBDto, badgeNumber);
        return ResponseEntity.ok("Badge scanned successfully for System B");
    }

    // Endpoint for assigning badge to Employee in system A
    @PostMapping("/systemA/assign/{employeeId}")
    public ResponseEntity<String> assignBadgeToEmployeeSystemA(@RequestBody BadgeSystemA_DTO badgeSystemADto, @PathVariable Long employeeId)
            throws NotFoundInDatabaseException {

        attendanceTrackingService.assignBadgeToEmployeeSystemA(badgeSystemADto, employeeId);
        return ResponseEntity.ok("Badge assigned successfully for System A");
    }

    //Endpoint for assigning badge to Employee in system B
    @PostMapping("/systemB/assign/{employeeId}")
    public ResponseEntity<String> assignBadgeToEmployeeSystemB(@RequestBody BadgeSystemB_DTO badgeSystemBDto, @PathVariable Long employeeId)
            throws NotFoundInDatabaseException {

        attendanceTrackingService.assignBadgeToEmployeeSystemB(badgeSystemBDto, employeeId);
        return ResponseEntity.ok("Badge assigned successfully for System B");
    }

}
