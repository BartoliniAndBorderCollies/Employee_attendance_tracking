package org.klodnicki.util;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.klodnicki.dto.employee.EmployeeDTORequest;

public class EmployeeImportMappingStrategy extends ColumnPositionMappingStrategy<EmployeeDTORequest> {

    public EmployeeImportMappingStrategy() {
        this.setType(EmployeeDTORequest.class);

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

