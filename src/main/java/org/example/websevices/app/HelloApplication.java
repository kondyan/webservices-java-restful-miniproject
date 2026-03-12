package org.example.websevices.app;

import jakarta.ws.rs.ApplicationPath;
import org.example.websevices.filter.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {
  public HelloApplication() {
    packages("org.example.websevices.resource");
    register(AuthenticationFilter.class);
  }
}