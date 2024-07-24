package org.klodnicki.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
}
