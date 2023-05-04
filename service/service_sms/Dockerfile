FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_sms service_sms.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_sms.jar","&"]

EXPOSE 8204