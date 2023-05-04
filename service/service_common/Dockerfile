FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_common service_common.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_common.jar","&"]
EXPOSE 8202