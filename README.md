# TheLoanWolves
Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

#### To make docker container for H2 database with volume in project folder run:

For LoginService
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoginService/data:/h2-data" --name h2-user -d -p 9091:9092 -p 8081:8082 buildo/h2database 
```
For LoanAmortizationService 
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoanAmortizationService/data:/h2-data" --name h2-loan -d -p 9093:9092 -p 8083:8082 buildo/h2database
```
For AccountService
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/AccountService/data:/h2-data" --name h2-account -d -p 9095:9092 -p 8085:8082 buildo/h2database 
```

#### To make docker container for Camunda, run:
```
docker pull camunda/camunda-bpm-platform:latest
```
```
docker run -d --name camunda -p 8080:8080 camunda/camunda-bpm-platform:latest
```
 
Go to [Camunda Dashboard](http://localhost:8080/camunda/app/cockpit/default/#/dashboard) to check tha Camunda is running. 

#### To make docker container for Kafka, run in the root of the downloaded project:
```
curl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-kafka/master/docker-compose.yml > docker-compose.yml
```
```
docker-compose up -d
```

1. Check in Docker that a container with a zookeeper and kafka is running. 
2. Next open OffSet Explorer 2 and add a connection to the created docker container.

3. Download Camunda Modeler.
 
With HomeBrew:
```
brew install --cask camunda-modeler
```
