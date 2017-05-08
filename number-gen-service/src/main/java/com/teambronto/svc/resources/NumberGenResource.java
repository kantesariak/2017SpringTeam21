package com.teambronto.svc.resources;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import com.google.common.base.Optional;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Random;

/**
 * Handles requests sent to the NumberGenService application.
 */
@Path("/number-gen-svc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NumberGenResource {
    /** An instance of Brave to use for tracing */
	private Brave brave;
	/** An instance of an HTTP client that is shared throughout the application to reduce overhead */
	private Client client;

	/**
	 * Constructs the resource class using an instance of brave and a Jersey client from NumberGenApplication
	 * @param brave the static instance of brave from NumberGenApplication, used for BraveTracingFeature
	 * @param client The Jersey Client being used to target the next application
	 */
	public NumberGenResource(Brave brave, Client client) {
		this.brave = brave;
		this.client = client;
	}

	/**
	 * Sends a request to /sleep-svc with an employee's ID and expects the data
	 * for that employee to be returned.
	 * 
	 * @param id The employee's ID.
	 * @return The employee data for the employee with the specified ID.
	 */
	@GET
	public String getEmployeeData(@QueryParam("id") String id) {
		WebTarget target = client.target("http://localhost:8889/sleep-svc")
				.queryParam("id", id);
		
		target.register(BraveTracingFeature.create(brave));

		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	/**
	 * Sends a request to /sleep-svc to create a table called employees
	 * 
	 * @return "create table employees".
	 */
	@GET
	@Path("/create-table")
	public String createTable() {
		WebTarget target = client.target("http://localhost:8889/sleep-svc/create-table");
		
		target.register(BraveTracingFeature.create(brave));

		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	/**
	 * Sends a request to /sleep-svc with a table name and delete that table
	 * 
	 * @param name
	 *            table name
	 * @return "delete: name".
	 */
	@GET
	@Path("/delete-table")
	public String deleteTable(@QueryParam("name") String name) {
		
		WebTarget target = client.target("http://localhost:8889/sleep-svc/delete-table")
				.queryParam("name", name);
		
		target.register(BraveTracingFeature.create(brave));

		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	/**
	 * Sends a request to /sleep-svc with row, first name, last name and email and update those values in the table
	 * 
	 * @param row 
	 * 			row number in the table
	 * @param first 
	 * 			first name
	 * @param last 
	 * 			last name
	 * @param email 
	 * 			email address
	 * @return "data updated".
	 */
	@GET
	@Path("/update-value")
	public String updateValue(@QueryParam("row") String row, @QueryParam("first") String first, @QueryParam("last") String last,
			@QueryParam("email") String email) {
		
		WebTarget target = client.target("http://localhost:8889/sleep-svc/update-value")
				.queryParam("row", row)
				.queryParam("first", first)
				.queryParam("last", last)
				.queryParam("email", email);
		
		target.register(BraveTracingFeature.create(brave));

		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	

}
