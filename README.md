# TheLoanWolves
#### Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

To make docker container with volume in project folder run:

For LoginService
```{r, engine='bash', count_lines}
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoginService/data:/h2-data" --name h2-user -d -p 9091:9092 -p 8081:8082 buildo/h2database 
```
For AccountService
```{r, engine='bash', count_lines}
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/AccountService/data:/h2-data" --name h2-account -d -p 9095:9092 -p 8085:8082 buildo/h2database 
```

 
