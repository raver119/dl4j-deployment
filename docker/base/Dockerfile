FROM ubuntu:bionic

RUN apt-get update && \
    apt-get -y --no-install-recommends install \
            wget vim inetutils-ping git scala openjdk-11-jre openjdk-11-jdk-headless maven gcc g++ automake cmake build-essential autoconf libtool pkg-config libgflags-dev libgtest-dev clang libc++-dev libgomp1 libprotobuf-dev && \
    apt-get clean
    #update-java-alternatives -s java-1.8.0-openjdk-amd64