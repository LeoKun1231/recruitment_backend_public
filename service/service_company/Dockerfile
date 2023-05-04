FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_company.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_company.jar","&"]
EXPOSE 8201