FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
#将镜像添加到容器内  target/${JAR_FILE}这是服务编译打包后的jar文件打包路径  /app.jar是容器内的jar文件路径
ADD ./target/${JAR_FILE} /service_user.jar
ENTRYPOINT ["java","-jar","-Xms256m","-Xmx256m","/service_user.jar","spring.profiles.active=prod","&"]

EXPOSE 8210