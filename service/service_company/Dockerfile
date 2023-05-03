FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/service_company service_company.jar
ENTRYPOINT ["java","-jar","/service_company.jar","&"]