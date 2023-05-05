FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /service_common.jar
ENTRYPOINT ["java","-jar","-Xms100m","-Xmx100m","/service_common.jar","&"]
EXPOSE 8202