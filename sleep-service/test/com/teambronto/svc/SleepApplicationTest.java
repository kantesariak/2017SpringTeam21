package com.teambronto.svc;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;
import com.google.common.net.InetAddresses;

/**
 * Tests SleepApplication
 * @author Xiangru Shu, Jackson Lu
 *
 */
public class SleepApplicationTest {
	
	 final SleepApplication app = new SleepApplication();
	 final SleepConfiguration config = new SleepConfiguration();

	 
	/**
     * Tests to see if brave is created
     */	  
	 @Test
	 public void testBraveInstanceInSleep() {
	    	Optional<Brave> brave = app.buildBrave(config.testGetReporter());
	    	assertNotNull(brave.get());
	    }
	 
	 /**
	  * A useful test that tests the functionality and usability of code, and is not designed
	  * for the sake of a class that requires JUnit tests/coverage.
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
	  * Tests getTraceId128Bit method
	  */
	 @Test
	 public void testGetName() {
		 assertEquals("SleepService", app.getName());
	 }

	 /**
	  * Tests getSampler method
	  */
	 @Test
	 public void testGetSampler(){
		 assertEquals(Sampler.create(1.0f), app.getSampler());
	 }
	 

}
