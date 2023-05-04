FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_company service_company.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_company.jar","&"]
EXPOSE 8201