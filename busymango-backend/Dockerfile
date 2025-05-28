# 使用JDK 8的基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将当前目录下的JAR包复制到容器的/app目录中
COPY busymangoBackend-0.0.1-SNAPSHOT.jar /app/busymangoBackend-0.0.1-SNAPSHOT.jar

# 暴露应用运行的端口（假设Spring Boot应用使用8085端口）
EXPOSE 8101

# 运行JAR包
ENTRYPOINT ["java", "-jar", "/app/busymangoBackend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
