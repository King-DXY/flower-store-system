# ============================================================
# Dockerfile — 鲜花商城 Spring Boot 应用镜像
# 多阶段构建：第一阶段用 Maven 编译，第二阶段用 JRE 运行
# 最终镜像只含运行需要的文件，体积小
# ============================================================

# ----- 第一阶段：编译 -----
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
# 先复制 pom.xml，利用 Docker 缓存层加速构建
COPY pom.xml .
RUN mvn dependency:go-offline -B
# 复制源码并编译
COPY src ./src
RUN mvn clean package -DskipTests -B

# ----- 第二阶段：运行 -----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# 从第一阶段复制 jar 包
COPY --from=builder /app/target/*.jar app.jar
# 创建上传目录
RUN mkdir -p uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
