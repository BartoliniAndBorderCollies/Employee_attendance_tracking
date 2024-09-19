package org.klodnicki.util;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.klodnicki.dto.employee.EmployeeDTOResponse;

public class EmployeeExportMappingStrategy extends ColumnPositionMappingStrategy<EmployeeDTOResponse> {

    public EmployeeExportMappingStrategy() {
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

    // Override generateHeader to return column names
    @Override
    public String[] generateHeader(EmployeeDTOResponse bean) {
        return new String[]{
                "First Name", "Last Name", "Email",
                "Street", "House Number", "Postal Code", "City", "Country",
                "Bank Account Number", "Birth Date", "Birth Place",
                "Date of Employment", "Department", "Gender", "ID", "PESEL/NIP",
                "Salary Amount",
                "Telephone Number"
        };
    }
}
