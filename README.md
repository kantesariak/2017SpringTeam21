# distributed-services
3 sample Dropwizard based services that are all sending requests to each other and traced by brave, of which one makes HBase calls. This is done automatically using BraveTracingFeature. HBase is being traced using localTracer() by wrapping all the methods.

To start the servers use run.bat
