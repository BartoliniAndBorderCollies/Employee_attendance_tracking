package org.klodnicki.util;

import org.modelmapper.PropertyMap;
import org.klodnicki.model.entity.Employee;
import org.klodnicki.dto.employee.EmployeeDTOResponse;

public class EmployeeToEmployeeDTOMapping extends PropertyMap<Employee, EmployeeDTOResponse> {
    @Override
    protected void configure() {
        // Map embedded Salary to salaryAmount
        map(source.getSalary().getAmount(), destination.getSalaryAmount());

        // Map embedded Address fields to flattened fields in EmployeeDTOResponse
        map(source.getAddress().getStreet(), destination.getStreet());
        map(source.getAddress().getHouseNumber(), destination.getHouseNumber());
        map(source.getAddress().getPostalCode(), destination.getPostalCode());
        map(source.getAddress().getCity(), destination.getCity());
        map(source.getAddress().getCountry(), destination.getCountry());
    }
}
