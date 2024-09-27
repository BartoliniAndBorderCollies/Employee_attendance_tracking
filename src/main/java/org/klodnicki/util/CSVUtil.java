package org.klodnicki.util;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.util.List;

public class CSVUtil {

    //this method will work with any type <T>, for example: List<Employee> T would be Employee, List<Badge> T would be Badge
    public static <T> void exportToCSV(String fileName, List<T> list, ColumnPositionMappingStrategy<T> strategy)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        try (FileWriter writer = new FileWriter(fileName)) {
            // Enable writing data with a semicolon as the separator
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withMappingStrategy(strategy)
                    .withSeparator(';')  // Use semicolon as the separator so that I have data in separate columns
                    .build();

            // Write data, StatefulBeanToCsv will handle headers automatically
            beanToCsv.write(list);
        }
    }

    public static <T> List<T> importFromCSV(InputStream inputStream, Class<T> clazz, MappingStrategy<T> strategy) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withMappingStrategy(strategy)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .withSkipLines(1)     // Skip the header line
                    .build();
            return csvToBean.parse();
        }
    }
}

