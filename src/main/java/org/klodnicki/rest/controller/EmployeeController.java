package org.klodnicki.rest.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.klodnicki.DTO.EmployeeDTORequest;
import org.klodnicki.DTO.EmployeeDTOResponse;
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


}
