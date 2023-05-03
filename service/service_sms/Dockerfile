FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_sms service_sms.jar
ENTRYPOINT ["java","-jar","/service_sms.jar","&"]