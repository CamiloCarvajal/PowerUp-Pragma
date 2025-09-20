
FROM eclipse-temurin:19-jdk AS builder
WORKDIR /workspace/app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

# Verificar que el bootJar se creó
RUN find . -name "*.jar" -type f

FROM eclipse-temurin:19-jre
WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /workspace/app/applications/app-service/build/libs/solicitud-service.jar app.jar

EXPOSE 8081
ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:MaxRAMPercentage=75.0"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar" ]