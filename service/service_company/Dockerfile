FROM openjdk:8
VOLUME /tmp
ADD ./target/service_company.jar /service_company.jar
ENTRYPOINT ["java","-jar","-Xms200m","-Xmx200m","/service_company.jar","&"]
EXPOSE 8201