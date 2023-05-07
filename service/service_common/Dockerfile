FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_common.jar
ENTRYPOINT ["java","-jar","-Xms256m","-Xmx256m","/service_common.jar","&"]
EXPOSE 8202