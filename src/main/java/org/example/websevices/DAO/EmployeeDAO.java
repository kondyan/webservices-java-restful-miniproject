package org.example.websevices.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.websevices.model.Employee;

import java.util.List;
import java.util.function.Function;

public class EmployeeDAO {

  private final Class<Employee> entityClass;

  @PersistenceContext
  private EntityManager em;

  public EmployeeDAO(Class<Employee> entityClass, EntityManager em) {
    this.entityClass = entityClass;
    this.em = em;
  }

  public Employee findById(Long id) {
    return executeInTransaction(em -> {
      return em.find(entityClass, id);
    });
  }

  public Employee findByName(String name) {
    return executeInTransaction(em -> {
      TypedQuery<Employee> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.name = :name",
                                                  entityClass);
      query.setParameter("name", name);
      return query.getSingleResult();
    });
  }

  public List<Employee> findAll() {
    return executeInTransaction(em -> {
      TypedQuery<Employee> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e",
                                                  entityClass);
      return query.getResultList();
    });
  }

  public Employee save(Employee entity) {
    return executeInTransaction(em -> {
      em.persist(entity);
      return entity;
    });
  }

  public Employee update(Employee entity) {
    return executeInTransaction(em -> {
      em.merge(entity);
      return entity;
    });
  }

  public Employee delete(Long id) {
    return executeInTransaction(em -> {
      Employee employee = em.find(entityClass, id);
      if (employee != null) {
        em.remove(employee);
      }
      return employee;
    });
  }

  public <R> R executeInTransaction(Function<EntityManager, R> action) {
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      R result = action.apply(em);
      tx.commit();
      return result;
    } catch (Exception e) {
      if (tx.isActive()) tx.rollback();
      throw e;
    }
  }
}
