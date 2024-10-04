package org.klodnicki.service;

import lombok.AllArgsConstructor;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.Action;
import org.klodnicki.model.entity.Badge;
import org.klodnicki.model.entity.BadgeScanHistory;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.BadgeRepository;
import org.klodnicki.repository.BadgeScanHistoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AttendanceTrackingService {

    private final BadgeRepository badgeRepository;
    private final BadgeScanHistoryRepository badgeScanHistoryRepository;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;


    //This method is used when the Employee gets his/her personal badge in System A tracking service
    public void assignBadgeToEmployeeSystemA(BadgeSystemA_DTO badgeSystemADto, Long employeeId) throws NotFoundInDatabaseException {

        Badge badge = new Badge();
        badge.setBadgeNumber(badgeSystemADto.getBadgeNumber());
        badge.setLocation(badgeSystemADto.getLocation());
        badge.setDeviceName(badgeSystemADto.getDeviceName());

        EmployeeDTOResponse employeeDTOResponse = employeeService.findById(employeeId);
        Employee mappedEmployee = modelMapper.map(employeeDTOResponse, Employee.class);

        badge.setEmployee(mappedEmployee);

        badgeRepository.save(badge);
    }

    //This method is used when the Employee gets his/her personal badge in System B tracking service
    public void assignBadgeToEmployeeSystemB(BadgeSystemB_DTO badgeSystemBDto, Long employeeId) throws NotFoundInDatabaseException {

        Badge badge = new Badge();
        badge.setBadgeNumber(badgeSystemBDto.getBadgeNumber());
        badge.setLocation(badgeSystemBDto.getLocation());
        badge.setDeviceName(badgeSystemBDto.getDeviceName());
        badge.setAction(Action.CLOCK_IN);
        badge.setTimeStamp(LocalDateTime.now());

        EmployeeDTOResponse employeeDTOResponse = employeeService.findById(employeeId);
        Employee mappedEmployee = modelMapper.map(employeeDTOResponse, Employee.class);

        badge.setEmployee(mappedEmployee);

        badgeRepository.save(badge);
    }

    private Badge findByBadgeNumber(String badgeNumber) throws NotFoundInDatabaseException {
        return badgeRepository.findByBadgeNumber(badgeNumber).orElseThrow(() -> new NotFoundInDatabaseException(Badge.class));
    }

    private Action determineAction(Badge badge) {
        BadgeScanHistory lastScan = badgeScanHistoryRepository.findFirstByBadgeOrderByTimeStampDesc(badge).orElse(null);
        return (lastScan == null || lastScan.getAction() == Action.CLOCK_IN) ? Action.CLOCK_OUT : Action.CLOCK_IN;
    }

    // method for enter/exit the work - system A
    public void scanBadgeSystemA(BadgeSystemA_DTO badgeSystemADto, String badgeNumber) throws NotFoundInDatabaseException {

        Badge badge = findByBadgeNumber(badgeNumber);

        BadgeScanHistory badgeScanHistory = new BadgeScanHistory();

        badgeScanHistory.setBadge(badge);
        badgeScanHistory.setLocation(badgeSystemADto.getLocation());
        badgeScanHistory.setDeviceName(badgeSystemADto.getDeviceName());
        badgeScanHistory.setTimeStamp(LocalDateTime.now());
        badgeScanHistory.setAction(determineAction(badge));

        Employee employee = badge.getEmployee();
        badgeScanHistory.setEmployee(employee);

        badgeScanHistoryRepository.save(badgeScanHistory);
    }

    // method for enter/exit the work - system B
    public void scanBadgeSystemB(BadgeSystemB_DTO badgeSystemBDto, String badgeNumber) throws NotFoundInDatabaseException {

        Badge badge = findByBadgeNumber(badgeNumber);

        BadgeScanHistory badgeScanHistory = new BadgeScanHistory();

        badgeScanHistory.setBadge(badge);
        badgeScanHistory.setLocation(badgeSystemBDto.getLocation());
        badgeScanHistory.setDeviceName(badgeSystemBDto.getDeviceName());
        badgeScanHistory.setAction(determineAction(badge));
        badgeScanHistory.setTimeStamp(LocalDateTime.now());
        Employee employee = badge.getEmployee();
        badgeScanHistory.setEmployee(employee);

        badgeScanHistoryRepository.save(badgeScanHistory);
    }


}
