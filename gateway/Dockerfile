FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /gateway.jar
ENTRYPOINT ["java","-jar","-Xms100m","-Xmx100m","/gateway.jar","&"]
EXPOSE 8666