package org.klodnicki.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.model.Department;
import org.klodnicki.model.Person;
import org.klodnicki.model.Salary;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    private Department department;
    private Salary salary;

    public Employee (Long id, String firstName, String lastName, String email, Department department, Salary salary) {
        super(id, firstName, lastName, email);
        this.department = department;
        this.salary = salary;
    }


}
