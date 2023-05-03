FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/gateway.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar","&"]