package org.klodnicki.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Address {

    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
}
