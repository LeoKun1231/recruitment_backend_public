FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_oss service_oss.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_oss.jar","&"]

EXPOSE 8205