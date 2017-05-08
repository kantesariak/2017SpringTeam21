@echo off
title start sleep
echo Starting sleep
cd sleep-service/target
java -jar sleep-service-1.0.0.jar server ../sleep-service.yml