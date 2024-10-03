FROM openjdk:8-jre-alpine
#EXPOSE 8082
ADD target/spring-security-oauth-backend.jar spring-security-oauth-backend.jar
ENTRYPOINT ["java", "-jar", "spring-security-oauth-backend.jar"]
MAINTAINER Kunal Chhetri <kunal.chhetri@tvsmotor.com>
# RUN apt-get update
CMD ["echo", "Creating a container of image"]


# FROM openjdk:8-jre-alpine
#
# RUN apk add --update tcpdump
#
# COPY target/comservice/comservice-* /comservice
#
# WORKDIR /comservice
#
# RUN mv comservice-*.jar comservice.jar
#
# ENTRYPOINT ["java","-Xms1g","-Xmx2g","-jar","comservice.jar"]