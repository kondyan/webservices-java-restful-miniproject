package org.example.websevices.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
  private static final String PERSISTENCE_UNIT_NAME = "EmployeePU";
  private static EntityManagerFactory entityManagerFactory;

  private JPAUtil() {
  }

  public static EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null || entityManagerFactory.isOpen()) {
      try {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        System.out.println("Entity Manager erfolgreich erstellt");
      } catch (Exception e) {
        System.out.println("Fehler beim Erstellen der EntityManagerFactory: " + e.getMessage());
        e.printStackTrace();
        throw new ExceptionInInitializerError(e);
      }
    }
    return entityManagerFactory;
  }

  public static void shutdown() {
    if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
      entityManagerFactory.close();
      System.out.println("EntityManagerFactory ist geschlossen.");
    }
  }
}
