package org.klodnicki.rest.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.klodnicki.DTO.Employee.EmployeeDTORequest;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.DTO.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTOResponse create(@RequestBody @Valid EmployeeDTORequest employeeDTO) {
        return employeeService.create(employeeDTO);
    }

    @GetMapping("/find/{id}")
    public EmployeeDTOResponse findById(@PathVariable Long id) throws NotFoundInDatabaseException {
        return employeeService.findById(id);
    }

    @GetMapping("/findAll")
    public List<EmployeeDTOResponse> findAll() {
        return employeeService.findAll();
    }

    @PutMapping("/update/{id}")
    public EmployeeDTOResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeDTORequest employeeDTO)
            throws NotFoundInDatabaseException {
        return employeeService.update(id, employeeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteById(@PathVariable Long id) throws NotFoundInDatabaseException {
        return employeeService.delete(id);
    }

}
