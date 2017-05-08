@echo off
title Start num-gen
echo Starting num-gen
cd number-gen-service/target
java -jar number-gen-service-1.0.0.jar server ../number-gen-service.yml
pause