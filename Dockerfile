FROM openjdk:11-jdk-slim

# Set working directory


# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
  unzip -qq awscliv2.zip && \
  ./aws/install --bin-dir /usr/bin --install-dir /usr/aws-cli --update && \
  aws --version
  
COPY ./script/script.sh /etc/script.sh
# Copy project files into the container
COPY . /app
WORKDIR /app
ENTRYPOINT ["/bin/bash", "/etc/script.sh"]


