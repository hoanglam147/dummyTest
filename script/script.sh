#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

# Use the CONFIG_FILE_PATH environment variable
echo "Using configuration file: $CONFIG_FILE_PATH"

# Perform operations with CONFIG_FILE_PATH
if [[ -f "$CONFIG_FILE_PATH" ]]; then
    echo "Configuration file found at $CONFIG_FILE_PATH"
else
    echo "Configuration file not found at $CONFIG_FILE_PATH"
    exit 1
fi

echo "Copying config file from S3..."
aws s3 cp $CONFIG_FILE_PATH /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}

echo "Running Maven tests..."
mvn test
