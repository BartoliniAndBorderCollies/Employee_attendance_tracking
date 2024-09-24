package org.klodnicki.dto.employee;

import com.opencsv.bean.CsvCustomBindByName;
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
import org.klodnicki.util.DepartmentConverter;
import org.klodnicki.util.LocalDateConverter;

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
    @CsvCustomBindByName(column = "Department", converter = DepartmentConverter.class)
    @NotNull(message = "Department must be chosen!")
    private Department department;
    @NotNull(message = "Salary must be given!")
    private double salaryAmount;  // Flattened Salary field
    private String birthPlace;
    @CsvCustomBindByName(column = "birthDate", converter = LocalDateConverter.class)
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
    @CsvCustomBindByName(column = "dateOfEmployment", converter = LocalDateConverter.class)
    private LocalDate dateOfEmployment;
    private BadgeSystemB_DTO badge;

}
