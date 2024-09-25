package org.klodnicki.util;

import org.klodnicki.dto.employee.EmployeeDTORequest;
import org.klodnicki.model.entity.Employee;
import org.modelmapper.PropertyMap;

public class EmployeeDTOToEmployeeMapping extends PropertyMap<EmployeeDTORequest, Employee> {
    @Override
    protected void configure() {
        // Map salaryAmount back to Salary entity
        map(source.getSalaryAmount(), destination.getSalary().getAmount());

        // Map flattened Address fields back to Address entity
        map(source.getStreet(), destination.getAddress().getStreet());
        map(source.getHouseNumber(), destination.getAddress().getHouseNumber());
        map(source.getPostalCode(), destination.getAddress().getPostalCode());
        map(source.getCity(), destination.getAddress().getCity());
        map(source.getCountry(), destination.getAddress().getCountry());

        // Directly map LocalDate fields
        map(source.getBirthDate(), destination.getBirthDate());
        map(source.getDateOfEmployment(), destination.getDateOfEmployment());
    }
}