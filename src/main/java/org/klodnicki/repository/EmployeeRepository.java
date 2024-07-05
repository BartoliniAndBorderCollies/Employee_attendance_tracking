package org.klodnicki.repository;

import org.klodnicki.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findById(Long id);
    List<Employee> findByLastName(String lastName);
}
