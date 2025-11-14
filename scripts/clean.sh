#!/bin/bash
./gradlew clean
docker system prune -a --volumes -f
echo "All clean!"
