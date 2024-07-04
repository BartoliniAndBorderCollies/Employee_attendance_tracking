package org.klodnicki.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.klodnicki.DTO.Employee.EmployeeDTORequest;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;
    private EmployeeService employeeService;
    private EmployeeDTORequest employeeDTORequest;
    private Employee employee;
    private EmployeeDTOResponse employeeDTOResponse;

    @BeforeEach
    public void prepareEnvironment() {
        employeeRepository = mock(EmployeeRepository.class);
        modelMapper = mock(ModelMapper.class);
        employeeService = new EmployeeService(employeeRepository, modelMapper);
    }

    @BeforeEach
    public void prepareInstances() {
        employeeDTORequest = new EmployeeDTORequest("firstName", "lastName", "email@email.com", Department.DEPARTMENT1,
                new Salary(100.00));
        employee = mock(Employee.class);
        employeeDTOResponse = mock(EmployeeDTOResponse.class);
    }

    @Test
    public void create_ShouldMapSaveAndMapAgain_WhenEmployeeDTORequestIsGiven() {
        //Arrange
        when(modelMapper.map(employeeDTORequest, Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeDTOResponse.class)).thenReturn(employeeDTOResponse);

        //Act
        EmployeeDTOResponse actualResponse = employeeService.create(employeeDTORequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(employeeDTOResponse, actualResponse);

        // Verify interactions with mocks
        verify(modelMapper).map(employeeDTORequest, Employee.class);
        verify(employeeRepository).save(employee);
        verify(modelMapper).map(employee, EmployeeDTOResponse.class);
    }

}