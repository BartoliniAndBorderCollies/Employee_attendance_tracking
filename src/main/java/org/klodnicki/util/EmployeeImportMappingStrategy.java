package org.klodnicki.util;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.klodnicki.dto.employee.EmployeeDTORequest;

public class EmployeeImportMappingStrategy extends ColumnPositionMappingStrategy<EmployeeDTORequest> {

    public EmployeeImportMappingStrategy() {
        this.setType(EmployeeDTORequest.class);

        this.setColumnMapping(
                "firstName", "lastName", "email",
                "street", "houseNumber", "postalCode", "city", "country",
                "bankAccountNumber", "rawBirthDate", "birthPlace",
                "rawDateOfEmployment", "department", "gender", "id", "peselOrNip",
                "salaryAmount", "telephoneNumber"
        );
    }
}