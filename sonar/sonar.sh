#!/bin/bash

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=loansimulator \
  -Dsonar.projectName='loansimulator' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=SEUTOKEN