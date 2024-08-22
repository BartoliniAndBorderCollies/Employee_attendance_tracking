package org.klodnicki.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.dto.badge.BadgeSystemB_DTO;
import org.klodnicki.model.Address;
import org.klodnicki.model.Department;
import org.klodnicki.model.Gender;
import org.klodnicki.model.Salary;

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
    @NotNull(message = "Departament must be chosen!")
    private Department department;
    @NotNull(message = "Salary must be given!")
    private Salary salary;
    private String birthPlace;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private String telephoneNumber;
    private String bankAccountNumber;
    private String peselOrNip;
    private LocalDate dateOfEmployment;
    private BadgeSystemB_DTO badge;

}
