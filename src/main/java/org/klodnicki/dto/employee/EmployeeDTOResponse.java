package org.klodnicki.dto.employee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.klodnicki.model.Department;
import org.klodnicki.model.Salary;

@Getter
@Setter
@Data
public class EmployeeDTOResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Department department;
    private Salary salary;
}
