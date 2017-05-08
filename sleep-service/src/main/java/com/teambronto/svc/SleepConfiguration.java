package com.teambronto.svc;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.okhttp3.OkHttpSender;

import javax.validation.constraints.NotNull;

public class SleepConfiguration extends Configuration {
	@NotNull
	@JsonProperty
	private String zipkinReportingAddress;

	String getZipkinReportingAddress() {
		return zipkinReportingAddress;
	}

	public Reporter<Span> getReporter() {
		return AsyncReporter.builder(getOkHttpSender()).build();
	}

	OkHttpSender getOkHttpSender() {
		return OkHttpSender.create(getZipkinReportingAddress());
	}
	
	public Reporter<Span> testGetReporter(){
		return AsyncReporter.builder(OkHttpSender.create("http://sd-vm23.csc.ncsu.edu:9411/api/v1/spans")).build();
	}
}
