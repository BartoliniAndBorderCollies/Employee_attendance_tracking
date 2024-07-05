package org.klodnicki.service;

import lombok.AllArgsConstructor;
import org.klodnicki.DTO.Employee.EmployeeDTORequest;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.DTO.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.klodnicki.service.generic.BasicCrudOperations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeService implements BasicCrudOperations<EmployeeDTOResponse, EmployeeDTORequest, Long> {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDTOResponse create(EmployeeDTORequest employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);

        return modelMapper.map(savedEmployee, EmployeeDTOResponse.class);
    }

    @Override
    public EmployeeDTOResponse findById(Long id) throws NotFoundInDatabaseException {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundInDatabaseException(Employee.class));
        return modelMapper.map(employee, EmployeeDTOResponse.class);
    }

    @Override
    public List<EmployeeDTOResponse> findAll() {
        List<EmployeeDTOResponse> employeeDTOResponseList = new ArrayList<>();
        Iterable<Employee> employees = employeeRepository.findAll();

        //use Stream and spliterator method in sequential mode(false) to map to DTO and add it straight to the list
        StreamSupport.stream(employees.spliterator(), false)
                .map(employee -> modelMapper.map(employee, EmployeeDTOResponse.class))
                .forEach(employeeDTOResponseList::add);

        return employeeDTOResponseList;
    }

    @Override
    public EmployeeDTOResponse update(Long id, EmployeeDTORequest employeeDTORequest) throws NotFoundInDatabaseException {
        Employee employeeToBeUpdated = employeeRepository.findById(id).orElseThrow(() -> new NotFoundInDatabaseException(Employee.class));

        Optional.ofNullable(employeeDTORequest.getFirstName()).ifPresent(employeeToBeUpdated::setFirstName);
        Optional.ofNullable(employeeDTORequest.getLastName()).ifPresent(employeeToBeUpdated::setLastName);
        Optional.ofNullable(employeeDTORequest.getEmail()).ifPresent(employeeToBeUpdated::setEmail);
        Optional.ofNullable(employeeDTORequest.getDepartment()).ifPresent(employeeToBeUpdated::setDepartment);
        Optional.ofNullable(employeeDTORequest.getSalary()).ifPresent(employeeToBeUpdated::setSalary);

        employeeRepository.save(employeeToBeUpdated);

        return modelMapper.map(employeeToBeUpdated, EmployeeDTOResponse.class);
    }

    @Override
    public ResponseDTO delete(Long id) throws NotFoundInDatabaseException {
        Employee employeeToBeDeleted = employeeRepository.findById(id).orElseThrow(() -> new NotFoundInDatabaseException(Employee.class));
        employeeRepository.delete(employeeToBeDeleted);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Employee " + employeeToBeDeleted.getFirstName() + " " + employeeToBeDeleted.getLastName() +
                " has been successfully deleted!");
        responseDTO.setStatus(HttpStatus.OK);

        return responseDTO;
    }
}
