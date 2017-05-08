package com.teambronto.svc;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import com.google.common.net.InetAddresses;
import com.teambronto.svc.resources.SleepResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.client.JerseyClientBuilder;
import zipkin.Span;
import zipkin.reporter.Reporter;

/**
 * SleepApplication is a Dropwizard application that acts as a middle-man between our other two services.
 * This allows us to see end-to-end tracing in zipkin with multiple services. It simply relays requests between
 * number-gen and htrave-service. In the future it would be useful to enable random network problems in this service
 * to simulate real-world communication problems.
 */
public class SleepApplication extends Application<SleepConfiguration> {
	/** The service's name */
	public static final String SERVICE_NAME = "SleepService";
	/** The service's URI */
	public static final String SERVICE_URI = "/sleep-svc";

	private Sampler sampler = null;
	private boolean traceId128Bit = false;

	public static void main(String[] args) {
		try {
			new SleepApplication().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	/**
     * Returns the sampler that determines how often events are reported to Zipkin
     */
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
		// this should be where zipkin is located
		final Brave brave = new Brave.Builder(toInt("152.14.106.33"), 9411, SERVICE_NAME).reporter(reporter)
				.traceSampler(getSampler()).traceId128Bit(traceId128Bit).build();

		return Optional.of(brave);
	}

	@Override
	public void run(SleepConfiguration config, Environment environment) throws Exception {
		Brave b = buildBrave(config.getReporter()).get();

		environment.jersey().register(new SleepResource(JerseyClientBuilder.newClient(), b));
		environment.jersey().register(BraveTracingFeature.create(b));
	}
}
