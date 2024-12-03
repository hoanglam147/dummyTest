FROM openjdk:11-jdk-slim
WORKDIR /app
COPY . /app
RUN ./mvnw package -DskipTests  # Build your app
CMD ["./mvnw", "test"]          # Run tests as default
