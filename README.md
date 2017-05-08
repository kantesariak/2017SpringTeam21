# distributed-services
3 sample Dropwizard based services that are all sending requests to each other, of which one makes HBase calls. This is done automatically using BraveTracingFeature. HBase is being traced using localTracer() by wrapping all the methods. All data is traced to a separate Zipkin server.

To start the servers use run.bat

After starting the servers visit http://localhost:8888/index.html for the demo. Visit Zipkin at sd-vm23.csc.ncsu.edu:9411. The Zipkin location will be outdated after 5/9/2017, so //TODO: change Zipkin location to localhost.
