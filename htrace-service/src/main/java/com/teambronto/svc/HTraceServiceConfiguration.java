package com.teambronto.svc;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.net.InetAddresses;
import io.dropwizard.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.okhttp3.OkHttpSender;

public class HTraceServiceConfiguration extends Configuration {
	@NotNull
	@JsonProperty
	private String zipkinReportingAddress;

	@NotNull
    @JsonProperty
    private String zipkinIp;

	@NotNull
    @JsonProperty
    private int zipkinPort;

	String getZipkinReportingAddress() {
		return zipkinReportingAddress;
	}

	/**
	 * Returns the HBase configuration to connect to the HBase server.
	 * @return The HBase configuration
	 */
	org.apache.hadoop.conf.Configuration getHBaseConfig() {
        org.apache.hadoop.conf.Configuration hbaseConfig = HBaseConfiguration.create();
        // There is the potential here to put these config variables into the htrace-service.yml config file.
        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseConfig.set("hbase.zookeeper.quorum", "localhost");
        return hbaseConfig;
    }

    OkHttpSender getOkHttpSender() {
        return OkHttpSender.create(getZipkinReportingAddress());
    }

    public Reporter<Span> getReporter() {
        return AsyncReporter.builder(getOkHttpSender()).build();
    }

    public Reporter<Span> testGetReporter(){
        return AsyncReporter.builder(OkHttpSender.create("http://sd-vm23.csc.ncsu.edu:9411/api/v1/spans")).build();
    }
}
