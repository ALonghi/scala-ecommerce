#!/bin/sh

echo "Creating local folders for logging.."
mkdir logs
chmod -R 777 logs
touch logs/scalacommerce-app.log
echo "Building Docker image of app.."
sbt Docker/publishLocal
echo "Cleaning previous docker data.."
docker compose rm -sf
echo "Running containers.."
docker compose up -d
echo "Connecting to application logs.."
tail -f logs/scalacommerce-app.log
