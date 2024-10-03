#!/bin/sh
mvn clean compile package

# build docker image
docker build -t tvsmazp360acrprod01.azurecr.io/spring-security-oauth-backend:1.0.0 .

docker push tvsmazp360acrprod01.azurecr.io/spring-security-oauth-backend:1.0.0
