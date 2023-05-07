FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_sms.jar
ENTRYPOINT ["java","-jar","-Xms100m","-Xmx100m","/service_sms.jar","--spring.profiles.active=prod","&"]

EXPOSE 8204