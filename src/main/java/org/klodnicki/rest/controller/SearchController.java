package org.klodnicki.rest.controller;

import lombok.AllArgsConstructor;
import org.klodnicki.DTO.Employee.EmployeeDTOResponse;
import org.klodnicki.model.Department;
import org.klodnicki.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchController {

    private final EmployeeService employeeService;

    @GetMapping("employee/name")
    public List<EmployeeDTOResponse> findByName (@RequestParam("lastName") String lastName) {
        return employeeService.findByName(lastName);
    }

    @GetMapping("employee/salary")
    public List<EmployeeDTOResponse> findBySalaryRange(@RequestParam("from") double from, @RequestParam("to") double to) {
        return employeeService.findBySalaryRange(from, to);
    }

    @GetMapping("employee/department")
    public List<EmployeeDTOResponse> findByDepartment (@RequestParam("department")Department department) {
        return employeeService.findByDepartment(department);
    }

}
