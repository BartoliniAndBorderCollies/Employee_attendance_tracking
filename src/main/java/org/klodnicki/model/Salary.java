package org.klodnicki.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

    @Min(value = 0, message = "Salary must be positive!")
    private double amount;
}
