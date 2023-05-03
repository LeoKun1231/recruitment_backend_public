FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_oss service_oss.jar
ENTRYPOINT ["java","-jar","/service_oss.jar","&"]