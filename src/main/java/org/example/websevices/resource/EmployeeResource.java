package org.example.websevices.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.websevices.model.Employee;
import org.example.websevices.repository.EmployeeRepository;

import java.util.List;


@Path("/employee")
public class EmployeeResource {
  @Inject
  private EmployeeRepository employeeRepository;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return employeeRepository.findById(id)
                             .map(employee -> Response.ok(employee)
                                                      .build())
                             .orElse(Response.status(Response.Status.NOT_FOUND)
                                             .type(MediaType.APPLICATION_JSON)
                                             .build());
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    List<Employee> employees = employeeRepository.findAll();
    return Response.ok(employees)
                   .build();
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByName(@QueryParam("name") String name) {
    return employeeRepository.findByName(name)
                             .map(employee -> Response.ok(employee)
                                                      .build())
                             .orElse(Response.status(Response.Status.NOT_FOUND)
                                             .type(MediaType.APPLICATION_JSON)
                                             .build());
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(@FormParam("name") String name, @FormParam("salary") Integer salary) {
    Employee employee = new Employee(name, salary);
    employeeRepository.save(employee);

    return Response.status(Response.Status.CREATED)
                   .entity(employee)
                   .build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    return employeeRepository.findById(id)
                             .map(employee -> {
                               employeeRepository.delete(id);
                               return Response.ok(employee)
                                              .build();
                             })
                             .orElse(Response.status(Response.Status.NOT_FOUND)
                                             .type(MediaType.APPLICATION_JSON)
                                             .build());


  }


}
