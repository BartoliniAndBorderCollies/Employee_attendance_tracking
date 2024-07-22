package org.klodnicki.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ResponseDTO {

    private String message;
    private HttpStatus status;

}
