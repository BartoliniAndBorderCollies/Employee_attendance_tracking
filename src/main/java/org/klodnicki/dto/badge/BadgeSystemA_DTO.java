package org.klodnicki.dto.badge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.dto.employee.EmployeeDTOResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeSystemA_DTO {

    private String badgeNumber;
    private String location;
    private String deviceName;
    @JsonBackReference  // to stop infinitive loop
    private EmployeeDTOResponse employee;
}
