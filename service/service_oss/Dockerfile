FROM openjdk:8
VOLUME /tmp
ADD ./target/service_oss.jar /service_oss.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_oss.jar","&"]

EXPOSE 8205