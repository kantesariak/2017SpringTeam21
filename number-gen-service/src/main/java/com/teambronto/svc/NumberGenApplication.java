package com.teambronto.svc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import zipkin.Span;
import zipkin.reporter.Reporter;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.JerseyClientBuilder;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import com.google.common.net.InetAddresses;
import com.teambronto.svc.resources.NumberGenResource;

/**
 * NumberGenApplication is a Dropwizard application that can perform various HTTP calls to modify an HBase database.
 * It has tracing enabled meaning that events are captured an reported to Zipkin.
 */
public class NumberGenApplication extends Application<NumberGenConfiguration> {
    /** The service's Name */
	public static final String SERVICE_NAME = "NumberGenService";
	/** The Service's URI */
	public static final String SERVICE_URI = "/number-gen-svc";

	public static void main(String[] args) {
		try {
			new NumberGenApplication().run(args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the traceID to 128bit instead of 64bit (default)
	 */
	private boolean traceId128Bit = false;

	/**
	 * This sampler improves zipkin peformance, where 0.0f is 0% and 1.0f is
	 * 100% of all traces being traced.
	 */
	private Sampler sampler = null;

	long traceID = 0L;

	@Override
	public String getName() {
		return SERVICE_NAME;
	}

	public boolean getTraceId128Bit() {
		return traceId128Bit;
	}

	public void setTraceId128Bit(boolean traceId128Bit) {
		this.traceId128Bit = traceId128Bit;
	}

	public long getTraceID() {
		return traceID;
	}

	public Sampler getSampler() {
		if (sampler == null) {
			// set sample rate to 100% by default.
			return Sampler.create(1.0f);
		}
		return sampler;
	}

	public void setSampler(Sampler sampler) {
		this.sampler = sampler;
	}

    /**
     * Converts a String IP address to an integer
     * @param ip The IP Address
     * @return The integer versio nof the ip address
     */
	private static int toInt(final String ip) {
		return InetAddresses.coerceToInteger(InetAddresses.forString(ip));
	}

    /**
     * Constructs an instance of Brave to use for tracing throughout the application
     * @param reporter The reporter that sends Spans to Zipkin.
     * @return The instance of Brave
     */
	public Optional<Brave> buildBrave(@Nonnull final Reporter<Span> reporter) {

		// zipkin location
		final Brave brave = new Brave.Builder(toInt("152.14.106.33"), 9411, SERVICE_NAME).reporter(reporter)
				.traceSampler(getSampler()).traceId128Bit(traceId128Bit).build();

		return Optional.of(brave);
	}
	
	@Override
	public void initialize(Bootstrap<NumberGenConfiguration> myConfigurationBootstrap) {
	    // This allows us to host a static demo webpage stored in the assets resource directory
	    myConfigurationBootstrap.addBundle(new AssetsBundle("/assets", "/", "assets/index.html"));
	}
	
	@Override
	public void run(NumberGenConfiguration config, Environment environment) throws Exception {
		//creates a client without a config, since dropwizard wasn't working with config
		//TODO: create Jersey client with config passed in
		new JerseyClientBuilder();
		final Client client = JerseyClientBuilder.newClient();
		
		Brave b = buildBrave(config.getReporter()).get();

		environment.jersey().setUrlPattern("/assets/*");
		environment.jersey().register(new NumberGenResource(b, client));
		environment.jersey().register(BraveTracingFeature.create(b));
	}
}
