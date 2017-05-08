package com.teambronto.svc.resources;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import com.teambronto.svc.SleepApplication;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * SleepResource handles all HTTP requests sent to the SleepService
 */
@Path(SleepApplication.SERVICE_URI)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SleepResource {
    /** A shared instance of Client */
	private Client client;
	/** A shared instance of Brave used for tracing HTTP requests */
	private Brave brave;

	public SleepResource(@Nonnull Client client, @Nonnull Brave brave) {
		this.client = client;
		this.brave = brave;
	}
	
	/**
	 * Simply returns employee's ID (not visible if the request was sent from another service)
	 * @param id the GET param (for example: url?id=3)
	 * @return String value of the id
	 */
	@GET
	public String getEmployeeData(@QueryParam("id") String id) {
		WebTarget target = client.target("http://sd-vm18.csc.ncsu.edu:8890/htrace-svc")
				.queryParam("id", id);
		target.register(BraveTracingFeature.create(brave));
		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	/**
	 * Create a table called employees
	 * @return "create table employees".
	 */
	@GET
	@Path("/create-table")
	public String createTable() {
		WebTarget target = client.target("http://sd-vm18.csc.ncsu.edu:8890/htrace-svc/create-table");
		target.register(BraveTracingFeature.create(brave));
		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	/**
	 * Delete an HBase table
	 * @param name The table's name.
	 * @return "delete: name".
	 */
	@GET
	@Path("/delete-table")
	public String deleteTable(@QueryParam("name") String name) {
		WebTarget target = client.target("http://sd-vm18.csc.ncsu.edu:8890/htrace-svc/delete-table")
				.queryParam("name", name);
		target.register(BraveTracingFeature.create(brave));
		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	/**
	 * Update values in the table
	 * @param row row number in the table
	 * @param first first name
	 * @param last last name
	 * @param email email address
	 * @return "data updated"
	 */
	@GET
	@Path("/update-value")
	public String updateValue(@QueryParam("row") String row, @QueryParam("first") String first, @QueryParam("last") String last,
			@QueryParam("email") String email) {
		WebTarget target = client.target("http://sd-vm18.csc.ncsu.edu:8890/htrace-svc/update-value")
				.queryParam("row", row)
				.queryParam("first", first)
				.queryParam("last", last)
				.queryParam("email", email);
		target.register(BraveTracingFeature.create(brave));
		return target.request(MediaType.APPLICATION_JSON).get(String.class);
	}

}
