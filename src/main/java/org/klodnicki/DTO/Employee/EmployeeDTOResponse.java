package org.klodnicki.DTO.Employee;

import lombok.Getter;
import lombok.Setter;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;

@Getter
@Setter
public class EmployeeDTOResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Department department;
    private Salary salary;
}
