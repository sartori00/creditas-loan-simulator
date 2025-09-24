#!/bin/bash

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=loansimulator \
  -Dsonar.projectName='loansimulator' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_e0885e69657ebeae8e1cd2f32aea3365cb1a2707