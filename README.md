# TheLoanWolves
#### Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

##### To make docker container for H2 database with volume in project folder run:

For LoginService
```{r, engine='bash', count_lines}
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoginService/data:/h2-data" --name h2-user -d -p 9091:9092 -p 8081:8082 buildo/h2database 
```
For LoanAmortizationService 
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoanAmortization/data:/h2-data" --name h2-loan -d -p 9093:9092 -p 8083:8082 buildo/h2database
```
For AccountService
```{r, engine='bash', count_lines}
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/AccountService/data:/h2-data" --name h2-account -d -p 9095:9092 -p 8085:8082 buildo/h2database 
```

##### To make docker container for Camunda, run:
```
docker pull camunda/camunda-bpm-platform:latest
```
```
docker run -d --name camunda -p 8080:8080 camunda/camunda-bpm-platform:latest
```
 
