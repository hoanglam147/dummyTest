#!/bin/bash
set -e  # Stop the script if any command fails

echo "Environment variables:"
printenv

S3_BUCKET="s3://testbuckethoanglam147/"
CONFIG_FILE="${S3_BUCKET}p17/a1.json"
echo "Using configuration file: $CONFIG_FILE"
# Perform operations with CONFIG_FILE_PATH
echo "Copying config file from S3..."
aws s3 cp $CONFIG_FILE /app/config.json || {
    echo "Failed to copy config from S3";
    exit 1;
}
echo "Running Maven tests..."
mvn clean test -Dmaven.test.failure.ignore=true
## add allure path
#curl -o allure-latest.zip https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.30.0/allure-commandline-2.30.0.zip
#unzip allure-latest.zip -d /opt/
#export PATH=$PATH:/opt/allure-2.30.0/bin
#
#echo 'export PATH=$PATH:/opt/allure-2.30.0/bin' >> ~/.bashrc
#source ~/.bashrc
## check version
#allure --version
#cd target/
#
#current_datetime=$(date +"%Y-%m-%d-%H-%M-%S")
#echo ${current_datetime}
#aws s3 cp ./allure-results ${S3_BUCKET_RESULT}/${current_datetime} --recursive
#
#if aws s3 ls "${S3_BUCKET_HISTORY}" > /dev/null 2>&1; then
#    echo "Directory exists in S3: ${S3_BUCKET_HISTORY}"
#    aws s3 cp ${S3_BUCKET_HISTORY} ./allure-results/history/ --recursive
#else
#    echo "Directory does not exist in S3: ${S3_BUCKET_HISTORY}"
#fi
#allure --version
#allure generate || { echo "Allure generation failed"; exit 1; }
#aws s3 cp ./allure-report ${S3_BUCKET_ALLURE_REPORT} --recursive
