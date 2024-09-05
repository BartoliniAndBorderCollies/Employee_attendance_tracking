package org.klodnicki.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.klodnicki.model.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee extends Person {

    public Employee (String firstName, String lastName, String email, Department department, Salary salary, String birthPlace,
                     LocalDate birthDate, Gender gender, Address address, String telephoneNumber, String bankAccountNumber,
                     String peselOrNip, LocalDate dateOfEmployment, Badge badge) {
        super(firstName, lastName, email);

        this.department = department;
        this.salary = salary;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.peselOrNip = peselOrNip;
        this.dateOfEmployment = dateOfEmployment;
        this.badge = badge;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING) //instead of 0,1,2 I want to keep Strings for clarity
    private Department department;
    @Embedded
    private Salary salary;
    private String birthPlace;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING) //instead of 0,1,2 I want to keep Strings for clarity
    private Gender gender;
    @Embedded
    private Address address;
    private String telephoneNumber;
    private String bankAccountNumber;
    private String peselOrNip;
    private LocalDate dateOfEmployment;
    @OneToOne(mappedBy = "employee")
    private Badge badge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(peselOrNip, employee.peselOrNip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peselOrNip);
    }
}
