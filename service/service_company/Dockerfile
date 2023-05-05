FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_company.jar
ENTRYPOINT ["java","-jar","-Xms100m","-Xmx100m","/service_company.jar","&"]
EXPOSE 8201