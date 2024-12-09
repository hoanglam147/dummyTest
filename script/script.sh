#!/bin/bash
printenv
echo Testing entrypoint
aws s3 cp s3://testbuckethoanglam147/p17/vault-P17 - Top 6 tests-1733466316298.json /app/config.json
mvn test