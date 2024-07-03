package org.klodnicki.service;

import lombok.AllArgsConstructor;
import org.klodnicki.DTO.EmployeeDTORequest;
import org.klodnicki.DTO.EmployeeDTOResponse;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.repository.EmployeeRepository;
import org.klodnicki.service.generic.BasicCrudOperations;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public EmployeeDTOResponse findById(Long id) {
        return null;
    }

    @Override
    public List<EmployeeDTOResponse> findAll() {
        return null;
    }

    @Override
    public EmployeeDTOResponse update(Long id, EmployeeDTORequest employee) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
