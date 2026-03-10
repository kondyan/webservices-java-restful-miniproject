package org.example.websevices.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "EmployeeID")
  private Long ID;

  @Column(name = "Name")
  private String name;

  @Column(name = "Salary")
  private Integer salary;

  public Employee() {
  }

  public Employee(String name, Integer salary) {
    this.name = name;
    this.salary = salary;
  }

  public Long getID() {
    return ID;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSalary() {
    return salary;
  }

  public void setSalary(Integer salary) {
    this.salary = salary;
  }
}