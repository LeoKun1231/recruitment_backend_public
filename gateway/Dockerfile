FROM openjdk:8
VOLUME /tmp
ADD ./target/gateway.jar /gateway.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/gateway.jar","&"]
EXPOSE 8666