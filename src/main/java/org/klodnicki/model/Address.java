package org.klodnicki.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {

    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
}
