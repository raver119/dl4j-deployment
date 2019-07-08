This toy project shows how to build production-like environment with Apache Kafka and neural networks served by DeepLearning4J in microservices architecture.

###Basic architecture:
- Apache Kafka cluster
- **fetcher** module to fetch web content
- **processor** module that operates on web content
- Neural network modules that provide various functionality via JSON/gRPC serving


###Requirements:
- Linux or MacOS (MS Windows will work too but build instructions probably won't)
- Docker and (optionally) Kubernetes

###Build instructions:
- `cd docker`
- `./build-docker-containers.sh`
- `docker-compuse build -f kafka-dl4j-classifier.yml`