#!/bin/sh
mvn clean compile package

# build docker image
docker build -t tvsmazcodpacrprod01.azurecr.io/spring-security-oauth-backend:1.0.1 .

docker push tvsmazcodpacrprod01.azurecr.io/spring-security-oauth-backend:1.0.1
