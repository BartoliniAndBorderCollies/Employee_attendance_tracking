package org.klodnicki.rest.controller;

import lombok.AllArgsConstructor;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.model.Department;
import org.klodnicki.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The SearchController class provides endpoints for searching employees
 * based on various criteria such as name, salary range, and department.
 * This controller is designed to be expanded in the future to support
 * additional search functionalities.
 */
@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchController {

    private final EmployeeService employeeService;

    /**
     * Finds employees by their last name.
     *
     * @param lastName The last name of the employees to search for.
     * @return A list of employees matching the given last name.
     */
    @GetMapping("employee/name")
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
    @GetMapping("employee/salary")
    public List<EmployeeDTOResponse> findBySalaryRange(@RequestParam("from") double from, @RequestParam("to") double to) {
        return employeeService.findBySalaryRange(from, to);
    }
    /**
     * Finds employees by their department.
     *
     * @param department The department to search for employees in.
     * @return A list of employees belonging to the specified department.
     */
    @GetMapping("employee/department")
    public List<EmployeeDTOResponse> findByDepartment (@RequestParam("department")Department department) {
        return employeeService.findByDepartment(department);
    }

}
