package org.klodnicki.dto.badge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.model.Action;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeSystemA_DTO {

    private String badgeNumber;
    private String location;
    private String deviceName;
    private Action action;
    private LocalDateTime timeStamp;
}
