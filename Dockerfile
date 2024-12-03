FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy project files into the container
COPY . /app

# Package the application using Maven
RUN mvn clean package -DskipTests

# Command to run tests or the application
CMD ["sh", "-c", "rm -rf /target/* && mvn test"]
