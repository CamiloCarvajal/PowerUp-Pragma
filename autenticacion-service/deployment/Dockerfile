# En autenticacion-service/deployment/Dockerfile
FROM eclipse-temurin:19-jdk AS builder
WORKDIR /workspace/app

# Copiar archivos de gradle
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY . .

# Dar permisos
RUN chmod +x ./gradlew

# Construir el proyecto
RUN ./gradlew clean build -x test --no-daemon

# Verificar que el bootJar se creó
RUN find . -name "*.jar" -type f

FROM eclipse-temurin:19-jre
WORKDIR /app

# Instalar curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copiar el bootJar del módulo app-service
COPY --from=builder /workspace/app/applications/app-service/build/libs/autenticacion-service.jar app.jar


EXPOSE 8080
ENV JAVA_OPTS="-Xms128m -Xmx512m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar" ]