package org.klodnicki.rest.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.klodnicki.dto.employee.EmployeeDTORequest;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.dto.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;
import org.klodnicki.model.Department;
import org.klodnicki.service.EmployeeService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final String EMPLOYEES_CSV = "employees.csv";

    @GetMapping("/export")
    public ResponseEntity<Resource> exportEmployeesToCSV() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        // Export employees to CSV
        employeeService.exportEmployeesToCSV(EMPLOYEES_CSV);

        File file = new File(EMPLOYEES_CSV);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Create resource for download
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);

        // Delete the file after sending the response
        file.delete();

        return response;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importEmployeesFromCSV() {
        try {
            employeeService.importEmployeesFromCSV(EMPLOYEES_CSV);
            return ResponseEntity.ok("Employee data imported successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing employee data.");
        }
    }

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
    @GetMapping(params = "lastName")
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
    @GetMapping(params = "department")
    public List<EmployeeDTOResponse> findByDepartment (@RequestParam("department") Department department) {
        return employeeService.findByDepartment(department);
    }

}

