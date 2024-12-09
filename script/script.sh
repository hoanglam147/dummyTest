#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

echo "Copying config file from S3..."
aws s3 cp s3://testbuckethoanglam147/p17/a1.json /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}

echo "Running Maven tests..."
mvn test
