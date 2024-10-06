package org.klodnicki.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTORequest {

    @NotBlank(message = "First name must be provided!")
    private String firstName;
    @NotBlank(message = "Last name must be provided!")
    private String lastName;
    @NotBlank(message = "Email address must be provided!")
    @Email(regexp=".+@.+\\..+", message = "Please enter a valid email address in the format: yourname@example.com")
    private String email;
    @NotNull(message = "Department must be chosen!")
    private Department department;
    private Double salaryAmount;  // Flattened Salary field
    private String birthPlace;

    // These are the fields I need to manually parse
    private String rawBirthDate;
    private String rawDateOfEmployment;

    private LocalDate birthDate;
    private LocalDate dateOfEmployment;
    private Gender gender;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
    private String telephoneNumber;
    private String bankAccountNumber;
    private String peselOrNip;
    private BadgeSystemB_DTO badge;

}
