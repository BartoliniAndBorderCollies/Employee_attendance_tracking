package org.klodnicki.util;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.klodnicki.model.entity.Employee;

public class EmployeeCSVMappingStrategy extends ColumnPositionMappingStrategy<Employee> {

    public EmployeeCSVMappingStrategy() {
        this.setType(Employee.class);

        this.setColumnMapping(
                "first_name", "last_name", "email",
                "address.street", "address.house_number", "address.postal_code", "address.city", "address.country",
                "badge.badge_number", "bank_account_number", "birth_date", "birth_place",
                "date_of_employment", "department", "gender", "id", "last_name", "pesel_or_nip", "salary_amount",
                "telephone_number"
        );
    }
}

