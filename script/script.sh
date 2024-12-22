#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

# Use the CONFIG_FILE_PATH environment variable
echo "Using configuration file: $CONFIG_FILE"
S3_BUCKET="s3://testbuckethoanglam147/"
S3_BUCKET_ALLURE_REPORT="${S3_BUCKET}${ENV}/allure-report"
S3_BUCKET_HISTORY="${S3_BUCKET_ALLURE_REPORT}/history"

S3_BUCKET_RESULT="${S3_BUCKET}${ENV}/allure-result"
# Perform operations with CONFIG_FILE_PATH

echo "Copying config file from S3..."
aws s3 cp $CONFIG_FILE /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}

echo "Running Maven tests..."
mvn clean test



# add allure path
curl -o allure-latest.zip https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.30.0/allure-commandline-2.30.0.zip
unzip allure-latest.zip -d /opt/
export PATH=$PATH:/opt/allure-2.30.0/bin

echo 'export PATH=$PATH:/opt/allure-2.30.0/bin' >> ~/.bashrc
source ~/.bashrc
# check version
allure --version
cd target/

current_datetime=$(date +"%Y-%m-%d-%H-%M-%S")
echo ${current_datetime}
aws s3 cp ./allure-results ${S3_BUCKET_RESULT}/${current_datetime} --recursive

if aws s3 ls "${S3_BUCKET_HISTORY}" > /dev/null 2>&1; then
    echo "Directory exists in S3: ${S3_BUCKET_HISTORY}"
    aws s3 cp ${S3_BUCKET_HISTORY} ./allure-results/history/ --recursive
else
    echo "Directory does not exist in S3: ${S3_BUCKET_HISTORY}"
fi
allure --version
allure generate || { echo "Allure generation failed"; exit 1; }
aws s3 cp ./allure-report ${S3_BUCKET_ALLURE_REPORT} --recursive


set -e

QUEUE_URL="https://sqs.ap-southeast-2.amazonaws.com/147997127717/quece-trigger-test"
TIMEOUT=300
CHECK_INTERVAL=30  # 5 minutes = 300 seconds

echo "Waiting for an SNS notification..."

# Track elapsed time
elapsed_time=0

while [ $elapsed_time -lt $TIMEOUT ]; do
    message=$(aws sqs receive-message \
        --queue-url $QUEUE_URL \
        --wait-time-seconds 10 \
        --query 'Messages[0].Body' \
        --output text)

    if [ -z "$message" ]; then
        echo "No message received in the last 10 seconds."
    else
        echo "Received message: $message"
        # Add logic to handle the message, e.g., trigger ECS tasks, rerun tests, etc.
    fi

    # Sleep for the next interval before checking again (5 minutes)
    sleep $CHECK_INTERVAL

    # Increment elapsed time
    elapsed_time=$((elapsed_time + CHECK_INTERVAL))
done

echo "Timeout reached, stopping the loop."