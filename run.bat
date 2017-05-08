@echo off
start "number-gen-service" java -jar number-gen-service\target\number-gen-service-1.0.0.jar server number-gen-service\number-gen-service.yml
start "sleep-service"      java -jar sleep-service\target\sleep-service-1.0.0.jar           server sleep-service\sleep-service.yml
::start "htrace-service"     java -jar htrace-service\target\htrace-service-1.0.0.jar         server htrace-service\htrace-service.yml
