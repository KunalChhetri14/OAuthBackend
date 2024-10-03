#!/bin/sh

#cd ../../
#mvn clean package
#unzip target/comservice-*-distribution.zip -d target/comservice
docker buildx create --name alexa-builder
docker buildx use alexa-builder
# build docker image
docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t  tvsmazcvdpacrdev01.azurecr.io/spring-security-oauth-backend:1.0.0 --push .

# push the docker image to Google Container Registry
#docker push tvsmazcvdpacrdev01.azurecr.io/spring-security-oauth-backend:1.0.7
docker buildx rm  alexa-builder
