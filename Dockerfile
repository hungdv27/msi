#FROM maven:3.6.3-jdk-11-slim AS MAVEN_BUILD
##FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD FOR JAVA 8
#
#ARG SPRING_ACTIVE_PROFILE
#
#MAINTAINER Jasmin
#COPY pom.xml /build/
#COPY src /build/src/
#WORKDIR /build/
#RUN mvn clean install -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE && mvn package -B -e -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE
#FROM openjdk:11-slim
##FROM openjdk:8-alpine FOR JAVA 8
#WORKDIR /app
#
#COPY --from=MAVEN_BUILD /build/target/*.jar /app/appdemo.jar
#ENTRYPOINT ["java", "-jar", "appdemo.jar"]

FROM bellsoft/liberica-openjre-alpine:17
ENV TZ="Asia/Ho_Chi_Minh"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
