package org.klodnicki.model.entity;

import jakarta.persistence.*;
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
@Entity
public class Employee extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Department department;
    @Embedded
    private Salary salary;

    public Employee (Long id, String firstName, String lastName, String email, Department department, Salary salary) {
        super(id, firstName, lastName, email);
        this.department = department;
        this.salary = salary;
    }


}
