#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

# Use the CONFIG_FILE_PATH environment variable
echo "Using configuration file: $CONFIG_FILE"

# Perform operations with CONFIG_FILE_PATH

echo "Copying config file from S3..."
aws s3 cp $CONFIG_FILE /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}

echo "Running Maven tests..."
mvn test
