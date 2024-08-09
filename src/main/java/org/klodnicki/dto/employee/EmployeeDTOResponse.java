package org.klodnicki.dto.employee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.model.Address;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;
import org.klodnicki.model.Salary;

import java.time.LocalDate;

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
    private String birthPlace;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private String telephoneNumber;
    private String bankAccountNumber;
    private String peselOrNip;
    private LocalDate dateOfEmployment;
    private BadgeSystemA_DTO badge;
}
