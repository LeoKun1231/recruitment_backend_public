FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_company.jar
ENTRYPOINT ["java","-jar","-Xms256m","-Xmx256m","/service_company.jar","spring.profiles.active=prod","&"]
EXPOSE 8201