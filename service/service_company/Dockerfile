FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/service_company.jar /service_company.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_company.jar","&"]
EXPOSE 8201