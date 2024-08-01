package org.klodnicki.dto.badge;

import lombok.Getter;
import lombok.Setter;
import org.klodnicki.dto.employee.EmployeeDTOResponse;
import org.klodnicki.model.Action;

import java.time.LocalDateTime;

@Getter
@Setter
public class BadgeSystemB_DTO {

    private String badgeNumber;
    private String location;
    private String deviceName;
    private Action action;
    private LocalDateTime timeStamp;
    private EmployeeDTOResponse employee;
}
