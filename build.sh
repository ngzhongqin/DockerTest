#!/usr/bin/env bash

mvn build
docker build ../DockerTest -t docker-test