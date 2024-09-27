package org.klodnicki.dto.employee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.klodnicki.dto.badge.BadgeSystemA_DTO;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;

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
    private double salaryAmount;  // Flattened Salary field
    private String birthPlace;
    private LocalDate birthDate;
    private Gender gender;

    // Flattened Address fields
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;

    private String telephoneNumber;
    private String bankAccountNumber;
    private String peselOrNip;
    private LocalDate dateOfEmployment;
    private BadgeSystemA_DTO badge;
}
