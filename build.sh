#!/usr/bin/env bash
# exit on error
set -o errexit

# Build the application
./mvnw clean package -DskipTests

echo "Build completed successfully!"