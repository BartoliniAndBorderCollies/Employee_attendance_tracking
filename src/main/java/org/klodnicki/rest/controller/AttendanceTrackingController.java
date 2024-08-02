package org.klodnicki.rest.controller;

import lombok.AllArgsConstructor;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.service.AttendanceTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/attendance")
public class AttendanceTrackingController {

    private final AttendanceTrackingService attendanceTrackingService;

    // Endpoint for handling badge data from System A
    @PostMapping("/systemA/scan")
    public ResponseEntity<String> scanBadgeSystemA(@RequestBody BadgeSystemA_DTO badgeSystemADto) {

        try {
            attendanceTrackingService.scanBadgeSystemA(badgeSystemADto);
            return ResponseEntity.ok("Badge scanned successfully for System A");
        } catch (NotFoundInDatabaseException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }




}
