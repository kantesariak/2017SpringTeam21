package com.teambronto.svc;

import zipkin.Span;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import zipkin.reporter.Reporter;

import java.util.Optional;

/**
 * This is just an example of how we could extract some of the behavior common between all of our Dropwizard
 * services that have tracing enabled. Eventually this could be rolled out to all 3 services in our project,
 * but for now, we have it just in HTraceService.
 */
public abstract class TracedApplication<T extends Configuration> extends Application<T> {
    /**
     * Sets the traceID to 128bit instead of 64bit (default)
     */
    final boolean traceId128Bit = false;

    /**
     * This sampler is for zipkin peformance, where 0.0f is 0% and 1.0f is
     * 100% of all traces being traced.
     */
    private Sampler sampler = null;

    abstract Optional<Brave> buildBrave(@NonNull T serviceConfig,
                              @NonNull Reporter<Span> reporter);

    Sampler getSampler() {
        if (sampler == null) {
            // set sample rate to 100% by default.
            return Sampler.create(1.0f);
        }
        return sampler;
    }
}