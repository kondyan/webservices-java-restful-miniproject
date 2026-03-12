package org.example.websevices.filter;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

  private static final String AUTHORIZATION_PROPERTY = "Authorization";
  private static final String AUTHENTICATION_SCHEME = "Basic";
  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Method method = resourceInfo.getResourceMethod();

    // 1. If @PermitAll is present, allow access
    if (method.isAnnotationPresent(PermitAll.class)) {
      return;
    }

    // 2. If @DenyAll is present, block access
    if (method.isAnnotationPresent(DenyAll.class)) {
      requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                                       .entity("Access blocked for all users")
                                       .build());
      return;
    }

    // 3. Extract Authorization Header
    final MultivaluedMap<String, String> headers = requestContext.getHeaders();
    final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

    if (authorization == null || authorization.isEmpty()) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                       .entity("Authentication required")
                                       .build());
      return;
    }

    // 4. Decode credentials (Basic Auth)
    final String encodedUserPassword = authorization.get(0)
                                                    .replaceFirst(AUTHENTICATION_SCHEME + " ", "");
    String usernameAndPassword = new String(Base64.getDecoder()
                                                  .decode(encodedUserPassword.getBytes()));
    final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
    final String username = tokenizer.nextToken();
    final String password = tokenizer.nextToken();

    // 5. Verify Role if @RolesAllowed is present
    if (method.isAnnotationPresent(RolesAllowed.class)) {
      RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
      Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

      // Logic to fetch user role from your system (hardcoded for example)
      String userRole = getUserRole(username, password);

      if (userRole == null || !rolesSet.contains(userRole)) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                         .entity("You do not have the required permissions")
                                         .build());
      }
    }
  }

  private String getUserRole(String username, String password) {
    // Integrate with your EmployeeRepository/Database here
    if ("admin".equals(username) && "password123".equals(password)) {
      return "ADMIN";
    }
    return "USER";
  }
}