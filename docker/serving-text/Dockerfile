FROM dl4j-deployment-base:1.0.0 as builder

EXPOSE 8081

CMD ["/usr/bin/java", "-cp", "/app/serving-text/target/serving-text-1.0-SNAPSHOT-bin.jar", "org.deeplearning4j.TextServingApp"]