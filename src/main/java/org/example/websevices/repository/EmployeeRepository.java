package org.example.websevices.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.websevices.DAO.EmployeeDAO;
import org.example.websevices.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository {
  //  private final Map<Long, Employee> table = new HashMap<>();

  private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
      "EmployeePU");

  private EmployeeDAO employeeDAO = new EmployeeDAO(Employee.class,
                                                    entityManagerFactory.createEntityManager());

  public EmployeeRepository(EmployeeDAO employeeDAO) {
    this.employeeDAO = employeeDAO;
  }

  public EmployeeRepository() {

  }

  public List<Employee> findAll() {
    return new ArrayList<>(employeeDAO.findAll());
  }

  public Optional<Employee> findById(Long id) {
    return Optional.ofNullable(employeeDAO.findById(id));
  }

  public Optional<Employee> findByName(String name) {
    return Optional.ofNullable(employeeDAO.findByName(name));
  }

  public void save(Employee employee) {
    if (employee.getID() == null) {
      employeeDAO.save(employee);
    } else {
      employeeDAO.update(employee);
    }
  }


  public void delete(Long id) {
    employeeDAO.delete(id);
  }

}
