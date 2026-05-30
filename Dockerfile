# Stage 1: build & test
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
LABEL maintainer="taewoo" stage="builder"

# JUnit 다운로드
RUN mkdir -p lib && \
    apt-get update && apt-get install -y curl && \
    curl -L -o lib/junit.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.7.1/junit-platform-console-standalone-1.7.1.jar

# 소스 복사 & 컴파일
COPY Test2/ ./Test2/
RUN mkdir -p classes && \
    cd Test2 && find src -name "*.java" > sources.txt && \
    javac -encoding UTF-8 -d ../classes -cp ../lib/junit.jar @sources.txt

# Stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/classes ./classes
COPY --from=builder /app/lib ./lib

# 컨테이너 실행 시 JUnit 테스트 자동 실행 (Docker 이미지 동작 증명용)
CMD ["java", "-jar", "lib/junit.jar", \
     "--class-path", "classes", \
     "--scan-class-path", \
     "--details=tree"]
