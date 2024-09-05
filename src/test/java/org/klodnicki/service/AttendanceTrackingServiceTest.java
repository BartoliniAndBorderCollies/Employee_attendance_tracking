package org.klodnicki.service;

import org.junit.jupiter.api.Test;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.entity.Badge;
import org.klodnicki.model.entity.BadgeScanHistory;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.BadgeRepository;
import org.klodnicki.repository.BadgeScanHistoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

class AttendanceTrackingServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private AttendanceTrackingService attendanceTrackingService;
    @Mock
    private BadgeRepository badgeRepository;
    private Long employeeId = 1L;
    @Mock
    EmployeeDTOResponse employeeDTOResponse;
    @Mock
    Employee employee = mock(Employee.class);
    @Mock
    private BadgeScanHistoryRepository badgeScanHistoryRepository;
    String badgeNumber = "12345";
    BadgeSystemA_DTO badgeSystemADto = new BadgeSystemA_DTO();
    BadgeSystemB_DTO badgeSystemBDto = new BadgeSystemB_DTO();


    public AttendanceTrackingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void assignBadgeToEmployeeSystemA_ShouldSetVariablesMapAndSave_WhenEmployeeIdAndBadgeDTOAAreGiven() throws NotFoundInDatabaseException {

        //Arrange
        BadgeSystemA_DTO badgeSystemADto = mock(BadgeSystemA_DTO.class);

        when(employeeService.findById(employeeId)).thenReturn(employeeDTOResponse);
        when(modelMapper.map(employeeDTOResponse, Employee.class)).thenReturn(employee);

        //Act
        attendanceTrackingService.assignBadgeToEmployeeSystemA(badgeSystemADto, employeeId);

        //Assert
        //Verify
        verify(modelMapper).map(employeeDTOResponse, Employee.class);
        verify(employeeService).findById(employeeId);
        verify(badgeRepository).save(any(Badge.class));
    }

    @Test
    public void assignBadgeToEmployeeSystemB_ShouldSetVariablesMapAndSave_WhenEmployeeIdAndBadgeDTOBAreGiven()
            throws NotFoundInDatabaseException {

        //Arrange
        BadgeSystemB_DTO badgeSystemBDto = mock(BadgeSystemB_DTO.class);

        when(employeeService.findById(employeeId)).thenReturn(employeeDTOResponse);
        when(modelMapper.map(employeeDTOResponse, Employee.class)).thenReturn(employee);

        //Act
        attendanceTrackingService.assignBadgeToEmployeeSystemB(badgeSystemBDto, employeeId);

        //Assert
        //Verify
        verify(modelMapper).map(employeeDTOResponse, Employee.class);
        verify(employeeService).findById(employeeId);
        verify(badgeRepository).save(any(Badge.class));
    }

    @Test
    public void ScanBadgeSystemA_ShouldCallOnRepositoryExactlyOnce_WhenBadgeDTOAAndBadgeNumberAreGiven() throws NotFoundInDatabaseException {
        // Arrange
        Badge badge = new Badge();
        when(badgeRepository.findByBadgeNumber(badgeNumber)).thenReturn(Optional.of(badge));

        // Act
        attendanceTrackingService.scanBadgeSystemA(badgeSystemADto, badgeNumber);

        // Assert
        verify(badgeScanHistoryRepository, times(1)).save(any(BadgeScanHistory.class));
    }

    @Test
    public void ScanBadgeSystemB_ShouldCallOnRepositoryExactlyOnce_WhenBadgeDTOBAndBadgeNumberAreGiven() throws NotFoundInDatabaseException {
        //Arrange
        Badge badge = new Badge();
        when(badgeRepository.findByBadgeNumber(badgeNumber)).thenReturn(Optional.of(badge));

        //Act
        attendanceTrackingService.scanBadgeSystemB(badgeSystemBDto, badgeNumber);

        //Assert
        verify(badgeScanHistoryRepository, times(1)).save(any(BadgeScanHistory.class));
    }

}