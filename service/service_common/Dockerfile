FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE}.jar /service_common.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_common.jar","&"]
EXPOSE 8202