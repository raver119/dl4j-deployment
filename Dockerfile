FROM dl4j-deployment-base:1.0.0 as builder

COPY / /app/

RUN cd /app && mvn -T 3 install -DskipTests=true

