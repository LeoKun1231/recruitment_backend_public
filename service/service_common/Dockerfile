FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_common service_common.jar
ENTRYPOINT ["java","-jar","/service_common.jar","&"]