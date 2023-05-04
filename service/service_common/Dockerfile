FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_common.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_common.jar","&"]
EXPOSE 8202