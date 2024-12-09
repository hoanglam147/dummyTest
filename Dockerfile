FROM openjdk:11-jdk-slim

# Set working directory


# Install Maven
RUN apt-get update && \
    apt-get install -y maven awscli unzip && \
    rm -rf /var/lib/apt/lists/*

COPY ./script/script.sh /etc/script.sh
# Copy project files into the container
COPY . /app
WORKDIR /app
ENTRYPOINT ["/bin/bash", "/etc/script.sh"]


