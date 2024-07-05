package org.klodnicki.repository;

import org.klodnicki.model.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findById(Long id);
    List<Employee> findByLastName(String lastName);
    @Query("SELECT e FROM Employee e WHERE e.salary.amount >= :from AND e.salary.amount < :to")
    List<Employee> findBySalaryRange(@Param("from") double from, @Param("to") double to);

}
