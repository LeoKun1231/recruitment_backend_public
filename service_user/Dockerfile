FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_user.jar service_user.jar
ENTRYPOINT ["java","-jar","/service_user.jar","&"]