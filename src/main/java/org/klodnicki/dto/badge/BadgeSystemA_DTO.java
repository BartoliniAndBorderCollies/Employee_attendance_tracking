package org.klodnicki.dto.badge;

import lombok.Getter;
import lombok.Setter;
import org.klodnicki.dto.employee.EmployeeDTOResponse;

@Getter
@Setter
public class BadgeSystemA_DTO {

    private String badgeNumber;
    private String location;
    private String deviceName;
    private EmployeeDTOResponse employee;
}
