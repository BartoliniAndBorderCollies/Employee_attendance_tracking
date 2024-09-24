package org.klodnicki.util;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.klodnicki.model.Department;

public class DepartmentConverter extends AbstractBeanField<Department, String> {

    @Override
    protected Department convert(String value) throws CsvDataTypeMismatchException {
        try {
            return Department.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CsvDataTypeMismatchException("Invalid value '" + value + "' for Department enum.");
        }
    }
}

