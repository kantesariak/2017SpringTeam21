package com.teambronto.svc.resources;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Test;

import com.github.kristofa.brave.Brave;
import com.teambronto.svc.SleepApplication;
import com.teambronto.svc.SleepConfiguration;
/**
 * Tests SleepResource
 * @author Xiangru Shu, Jackson Lu
 *
 */
public class SleepResourceTest {
	/*
	 * set up
	 */
    SleepApplication app = new SleepApplication();
    SleepConfiguration config = new SleepConfiguration();
    Brave b = app.buildBrave(config.testGetReporter()).get();
    Client client = JerseyClientBuilder.newClient();
    SleepResource resource = new SleepResource(client, b);
    
    /**
     * Tests getEmployeeDataById method
     */
    @Test
    public void testGetEmployeeDataById(){
    	resource.createTable();
    	resource.updateValue("row1", "xiangru", "shu", "xshu3@ncsu.edu");
    	String result = "{\"employee\":{\"firstName\":\"xiangru\",\"lastName\":\"shu\",\"email\":\"xshu3@ncsu.edu\",\"rowId\":\"row1\"}}";
    	assertEquals(result, resource.getEmployeeData("row1"));
    	String result1 = "{\"employee\":{\"firstName\":\"null\",\"lastName\":\"null\",\"email\":\"null\",\"rowId\":\"row1000\"}}";
    	assertEquals(result1, resource.getEmployeeData("row1000"));
    	resource.deleteTable("employees"); 	
    }
    /**
     * Tests createTable method
     */
	@Test
	public void testCreateTable() {
		assertEquals("Creating table employees",resource.createTable());
		assertEquals("Found table employees",resource.createTable());
		resource.deleteTable("employees");		
	}
	/**
     * Tests deleteTable method
     */
    @Test
	public void testDeleteTable() {
    	resource.createTable();
    	assertEquals("delete: employees",resource.deleteTable("employees"));    	
    	assertEquals("employees does not exist ",resource.deleteTable("employees"));
	 }
    
    /**
     * Tests updateValue method
     */
    @Test
	public void testUpdateValue() {
    	resource.createTable();
   		assertEquals("data Updated",resource.updateValue("roafw1", "ad", "shufadfa", "xshuFa3"));
   		resource.deleteTable("employees");
	}
   


}
