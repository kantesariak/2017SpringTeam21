package com.teambronto.svc;

import org.junit.*;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;

import static org.junit.Assert.*;

import java.util.Optional;
/**
 * Tests NumberGenApplication
 * @author Xiangru Shu, Jackson Lu
 *
 */
public class NumberGenApplicationTest {
    final NumberGenApplication app = new NumberGenApplication();
    final NumberGenConfiguration config = new NumberGenConfiguration();
    
    public NumberGenApplicationTest() {
    	//start "number-gen-service" java -jar number-gen-service\target\number-gen-service-1.0.0.jar server number-gen-service\number-gen-service.yml
    }
    
    /**
     * Tests to see if brave is created
     */
    @Test
    public void testBraveInstance(){
    	Optional<Brave> brave = app.buildBrave(config.testGetReporter());
    	assertNotNull(brave.get());

    }
    
    /**
     * Tests getTraceId128Bit method
     */
    @Test
	 public void testTraced128bit() {
		 assertEquals(false, app.getTraceId128Bit());
		 app.setTraceId128Bit(true);
		 assertEquals(true, app.getTraceId128Bit());
		 app.setTraceId128Bit(false);
		 assertEquals(false, app.getTraceId128Bit());
		 
	 }
    
	 /**
	  * Tests getName method
	  */
	 @Test
	 public void testGetName() {
		 assertEquals("NumberGenService", app.getName());
	 }

	 /**
	  * Tests getSampler method
	  */
	 @Test
	 public void testGetSampler(){
		 assertEquals(Sampler.create(1.0f), app.getSampler());
	 }
    
}