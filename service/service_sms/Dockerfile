FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_sms.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_sms.jar","&"]

EXPOSE 8204