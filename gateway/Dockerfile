FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ./target/gateway.jar /gateway.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/gateway.jar","&"]
EXPOSE 8666