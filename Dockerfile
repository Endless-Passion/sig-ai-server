# 멀티 스테이지 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 캐시 활용을 위해 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle

# 소스 코드 복사 및 빌드
COPY src src
RUN gradle clean bootJar --no-daemon

# 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
