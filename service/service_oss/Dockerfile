FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE}.jar /service_oss.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_oss.jar","&"]

EXPOSE 8205