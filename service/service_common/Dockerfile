FROM openjdk:8
VOLUME /tmp
ADD ./target/service_common.jar /service_common.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_common.jar","&"]
EXPOSE 8202