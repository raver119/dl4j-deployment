FROM dl4j-deployment-java:1.0.0 as builder

EXPOSE 8080

CMD ["/usr/bin/java", "-jar", "/app/fetcher/target/fetcher-1.0-SNAPSHOT-bin.jar", "org.deeplearning4j.FetcherApp"]