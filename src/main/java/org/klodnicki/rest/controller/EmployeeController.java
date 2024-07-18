package org.klodnicki.rest.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.klodnicki.dto.employee.EmployeeDTORequest;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.dto.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.Department;
import org.klodnicki.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTOResponse create(@RequestBody @Valid EmployeeDTORequest employeeDTO) {
        return employeeService.create(employeeDTO);
    }

    @GetMapping("/{id}")
    public EmployeeDTOResponse findById(@PathVariable Long id) throws NotFoundInDatabaseException {
        return employeeService.findById(id);
    }

    @GetMapping
    public List<EmployeeDTOResponse> findAll() {
        return employeeService.findAll();
    }

    @PutMapping("/{id}")
    public EmployeeDTOResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeDTORequest employeeDTO)
            throws NotFoundInDatabaseException {
        return employeeService.update(id, employeeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteById(@PathVariable Long id) throws NotFoundInDatabaseException {
        return employeeService.delete(id);
    }

    /**
     * Finds employees by their last name.
     *
     * @param lastName The last name of the employees to search for.
     * @return A list of employees matching the given last name.
     */
    @GetMapping("/name")
    public List<EmployeeDTOResponse> findByName (@RequestParam("lastName") String lastName) {
        return employeeService.findByName(lastName);
    }

    /**
     * Finds employees within a specified salary range.
     *
     * @param from The minimum salary.
     * @param to The maximum salary.
     * @return A list of employees whose salaries fall within the specified range.
     */
    @GetMapping("/salary")
    public List<EmployeeDTOResponse> findBySalaryRange(@RequestParam("from") double from, @RequestParam("to") double to) {
        return employeeService.findBySalaryRange(from, to);
    }
    /**
     * Finds employees by their department.
     *
     * @param department The department to search for employees in.
     * @return A list of employees belonging to the specified department.
     */
    @GetMapping("/department")
    public List<EmployeeDTOResponse> findByDepartment (@RequestParam("department") Department department) {
        return employeeService.findByDepartment(department);
    }
}

