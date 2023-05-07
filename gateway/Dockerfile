FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} /gateway.jar
ENTRYPOINT ["java","-jar","-Xms256m","-Xmx256m","/gateway.jar", "--spring.profiles.active=prod","&"]
EXPOSE 8666