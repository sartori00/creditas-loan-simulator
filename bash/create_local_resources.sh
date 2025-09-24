#!/bin/bash

aws --endpoint=http://localhost:4566 sqs create-queue --queue-name sendmail-queue --attributes VisibilityTimeout=60

echo "A queue foi com sucesso!"