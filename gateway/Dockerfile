FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target${JAR_FILE} /gateway.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/gateway.jar","&"]
EXPOSE 8666