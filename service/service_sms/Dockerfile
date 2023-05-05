FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/service_sms.jar /service_sms.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_sms.jar","&"]

EXPOSE 8204