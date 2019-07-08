#!/usr/bin/env bash

set -e

# base image: java, cpp and so on
docker build -t dl4j-deployment-base:1.0.0 ./base

# base java apps built at once
docker build -t dl4j-deployment-java:1.0.0 ../

# these two are just entries for specific modules
docker build -t dl4j-deployment-fetcher:1.0.0 ./fetcher
docker build -t dl4j-deployment-processor:1.0.0 ./processor

# these two are model serving modules
docker build -t dl4j-deployment-model-image:1.0.0 ./serving-image
docker build -t dl4j-deployment-model-text:1.0.0 ./serving-text