package org.klodnicki.util;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.klodnicki.dto.employee.EmployeeDTOResponse;

public class EmployeeCSVMappingStrategy extends ColumnPositionMappingStrategy<EmployeeDTOResponse> {

    public EmployeeCSVMappingStrategy() {
        this.setType(EmployeeDTOResponse.class);

        // Map columns including the flattened fields
        this.setColumnMapping(
                "firstName", "lastName", "email",  // From Person class
                "street", "houseNumber", "postalCode", "city", "country",  // Flattened Address fields
                "bankAccountNumber", "birthDate", "birthPlace",
                "dateOfEmployment", "department", "gender", "id", "peselOrNip",
                "salaryAmount",  // Flattened Salary amount
                "telephoneNumber"
        );
    }
}
