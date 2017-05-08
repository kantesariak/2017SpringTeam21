package com.teambronto.svc;

import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;

import com.google.common.net.InetAddresses;

import zipkin.Span;
import com.github.kristofa.brave.Brave;
import io.dropwizard.setup.Environment;
import com.teambronto.svc.resources.HTraceServiceResource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import zipkin.reporter.Reporter;
import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * This is our HTrace Service Dropwizard application. It handles REST calls to modify the HBase directory.
 * Tracing is enabled so Zipkin can see events that take place.
 */
public class HTraceServiceApplication extends TracedApplication<HTraceServiceConfiguration> {
    private static final String SERVICE_NAME = "HTrace Service";
    private Table table;

    public static void main(String[] args) {
        try {
            new HTraceServiceApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(HTraceServiceConfiguration serviceConfig, Environment environment) throws Exception {
        final Brave brave = buildBrave(serviceConfig, serviceConfig.getReporter()).get();

        // Create a traced connection to the HBase database.
        final Configuration hbaseConfig = serviceConfig.getHBaseConfig();

        // Get a traced instance of the Connection and Admin classes to use in the HTraceServiceResource class.
        final Connection tracedConn = new TracedConnection(ConnectionFactory.createConnection(hbaseConfig), brave);
        Admin admin = tracedConn.getAdmin();

        environment.jersey().register(new HTraceServiceResource(brave, tracedConn));
        environment.jersey().register(BraveTracingFeature.create(brave));
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    /**
     * Creates a single instance of brave to use throughout the application.
     * @param serviceConfig The configuration information for this service.
     * @param reporter The reporter with which to send Spans to Zipkin.
     * @return An instance of Brave.
     */
    Optional<Brave> buildBrave(@NotNull HTraceServiceConfiguration serviceConfig,
                               @NotNull Reporter<Span> reporter) {
//        final Endpoint braveEndpoint = new Endpoint(toInt("152.14.106.33"), (short)9411, getName());
        final Brave brave = new Brave.Builder(toInt("152.14.106.33"), 9411, getName())
                .reporter(reporter)
                .traceSampler(getSampler())
                .traceId128Bit(traceId128Bit)
                .build();
        return Optional.of(brave);
    }

    /**
     * Convert a string IP address into an integer.
     * @param ip The IP string
     * @return The integer representation of the IP address.
     */
    private static int toInt(final String ip) {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ip));
    }
}
