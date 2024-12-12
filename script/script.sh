#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

# Use the CONFIG_FILE_PATH environment variable
echo "Using configuration file: $CONFIG_FILE"
S3_BUCKET_HISTORY="s3://testbuckethoanglam147/$ENV/allure-report/history"
S3_BUCKET="s3://testbuckethoanglam147/$ENV/allure-report"
S3_BUCKET_RESULT="s3://testbuckethoanglam147/$ENV/allure-result"
# Perform operations with CONFIG_FILE_PATH

echo "Copying config file from S3..."
aws s3 cp $CONFIG_FILE /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}

echo "Running Maven tests..."
mvn clean test
current_datetime=$(date +"%Y-%m-%d-%H-%M-%S")


# add allure path
curl -o allure-latest.zip https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.30.0/allure-commandline-2.30.0.zip
unzip allure-latest.zip -d /opt/
export PATH=$PATH:/opt/allure-2.30.0/bin

echo 'export PATH=$PATH:/opt/allure-2.30.0/bin' >> ~/.bashrc
source ~/.bashrc
# check version
allure --version
cd target/
echo ${current_datetime}
aws s3 cp ./allure-results ${S3_BUCKET_RESULT}/${current_datetime} --recursive

if aws s3 ls "${S3_BUCKET_HISTORY}" > /dev/null 2>&1; then
    echo "Directory exists in S3: ${S3_BUCKET}"
    aws s3 cp ${S3_BUCKET_HISTORY} ./allure-results/history/ --recursive
else
    echo "Directory does not exist in S3: ${S3_BUCKET_HISTORY}"
fi
allure --version
allure generate || { echo "Allure generation failed"; exit 1; }
aws s3 cp ./allure-report ${S3_BUCKET} --recursive


#!/bin/bash

# Trigger Lambda function (if needed)
# ...

# Set a timeout of 5 minutes (300 seconds)
timeout 300 aws sns receive --topic-arn arn:aws:sns:ap-southeast-2:147997127717:notification-when-launch-ec2-instance --wait-time-seconds 10

# Check if the `aws sns receive` command timed out
if [ $? -eq 124 ]; then
  echo "SNS notification timeout. Exiting."
  exit 1
fi

# Parse the message
message=$(jq -r '.Message' <<< "$REPLY")

# Parse the JSON message
result=$(jq -r '.test_result' <<< "$message")
reason=$(jq -r '.reason' <<< "$message")
re_run=$(jq -r '.re_run' <<< "$message")

if [[ $result == "failed" ]]; then
  echo "Test failed: $reason"
  if [[ $re_run == "true" ]]; then
    echo "Re-running tests..."
    # Run your test scripts here
  fi
else
  echo "Test passed successfully"
fi