FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_user.jar service_user.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_user.jar","&"]

EXPOSE 8201