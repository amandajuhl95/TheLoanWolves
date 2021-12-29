# The Loan Wolves
Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

#### Project Description

Loan Wolves is a swedish cooperative bank. Seated in Göteborg. They would like to open a department in Denmark. Their IT system is outdated so they would like to make a new one for the Danish department. Both systems should have the same functionality but the new one should be improved with performance in mind. The Loan Wolves will offer the same loan types in Denmark as they do in Sweden. Therefore, the new IT system should have access to the loan types that the bank offers. The loan types are stored in csv file(s) and are updated daily, by the current IT system. Doing the last couple of years, The Loan Wolves have received an increasing amount of loan requests in other currencies than SEK, therefore they have created an internal service to convert the amounts from international currencies. This service is separated from their main system. The service is accessed through a REST Api and can be used by the Danish IT-system.

#### Legacy System

* More difficult to develop new functionality given it’s a monolithic setup.
* It is not possible to scale the most used parts of the system, it's only possible to scale the entire system.
* Inconsistent architecture with only one microservice.

* The CSV file(s) and the internal conversion service will be used in our new system.

![](https://github.com/amalielandt/TheLoanWolves/blob/main/diagrams/legacy_setup.PNG)

#### To make docker container for H2 database with volume in project folder run:

For LoginService 
<i>Go to: localhost:8081</i>
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoginService/data:/h2-data" --name h2-user -d -p 9091:9092 -p 8081:8082 buildo/h2database 
```
For LoanAmortizationService and LoanQuoteService 
<i>Go to: localhost:8083</i>
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoanAmortizationService/data:/h2-data" --name h2-loan -d -p 9093:9092 -p 8083:8082 buildo/h2database
```
For AccountService 
<i>Go to: localhost:8085</i>
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/AccountService/data:/h2-data" --name h2-account -d -p 9095:9092 -p 8085:8082 buildo/h2database 
```
For LoggingService 
<i>Go to: localhost:8089</i>
```
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/TheLoanWolves/LoggingService/data:/h2-data" --name h2-logging -d -p 9099:9092 -p 8089:8082 buildo/h2database
```

#### To make docker container for Camunda, run:
```
docker pull camunda/camunda-bpm-platform:latest
```
```
docker run -d --name camunda -p 8080:8080 camunda/camunda-bpm-platform:latest
```
 
1. Go to [Camunda Dashboard](http://localhost:8080/camunda/app/cockpit/default/#/dashboard) to check tha Camunda is running. 

##### Login information
- Username = demo
- Password = demo

2. Click on "Process Definitions" 
3. Click on "SI_exam_Loan_picker_process"
4. This BPMN-diagram where you can see when processes is running and how far a task are in the flow.
5. Download Camunda Modeler.
 
#### To make docker container for Kafka, run in the root of the downloaded project:
```
curl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-kafka/master/docker-compose.yml > docker-compose.yml
```
```
docker-compose up -d
```

1. Check in Docker that a container with a zookeeper and kafka is running. 
2. Next open OffSet Explorer 2 and add a connection to the created docker container.

#### Endpoints

- All endpoints requires that you are logged in (besides create user).
- When logged in an autogenerated token is returned, and this should be send as part of Request Headers {key:value}, within the following requests.

```
Headers = {Session-Token:"autogenerated token"} 
```

localhost:8070/loan-wolves

1. Login[POST]: /login
```
@RequestBody {"cpr":String, "password":String}
```

2. Create User[POST]: /new/user
```
@RequestBody {"cpr":String, "fullName":String, "email":String, "password":String, "phoneNumber":String, "salary":Double, "type":String, "Address": {"number":String, "street":String, "city":String, "zipcode":Integer}} 
```

3. Update User[PUT]: /update/user 
```
@RequestBody {"cpr":String, "oldPassword":String, "newPassword":String, "fullName":String, "email":String, "phoneNumber":String, "salary":Double, "Address": {"street":String, "number":String, "city":String, "zipcode":Integer}} 
```

4. Retrieve One User[GET]: /user/{cpr}
```
cpr = 11 characters (eg. "101099-1234")
```

5. Create Account[POST]: /new/account/{type}/{userId}
```
type = SAVINGS, SALARY, PAYMENT or DEBIT
```

6. Retrieve one Account[GET]: /account/{userId}/{accountId}

7. Retrieve all Accounts[GET]: /account/{userId}

8. Make transaction[POST]: /account/transaction/{userId}/{accountId}
```
@RequestBody {"amount":Double, "type":type} 
```

9. Request loan[POST]: /loan/request/{userId} 
```
@RequestBody {"amount":Double} 
```

10. Loan decision[POST]: /loan/decision/{status}/{userId}/{loanQuoteId}
```
status = ACCEPTED or DECLINED
```

11. Make amortization[POST]: /loan/amortization/{userId}/{accountId}/{loanId}
```
@RequestBody {"amount":Double. "type":type} 
```

#### Camunda Diagram

A BPMN Camunda process is used to determine whether a customer is viable for at loan or not. Once a request is sent to Camunda the liability of the customer is automatically determined. This is done with an external python worker that looks at the customers request and personal information, such as his/hers age, salary ect. 

* If the loan request is above a certain amount an employee looks at the offer manually. 
* If an employee has not looked at the offer within two days, then the loan request is cancelled. 
* If it is looked at and accepted, an email is automatically send to the customer with a contract. In the contract the customer can se all terms of the loan such as the interest rate, fee, and duration of the loan. 
* If the loan was not accepted by the system or employee, the customer's loan request is declined and is informed by an email. 

Finally, all accepted requests are saved. This is done by sending the loan request over kafka to a handler, that saves the loan requests in a database.

![](https://github.com/amalielandt/TheLoanWolves/blob/main/diagrams/camunda_diagram.png)

#### New setup

* By using microservices we create a scalable setup. Where it is easy to implement new functionality and a high uptime can be guaranteed.
* gRPC is used to create, update and login a user.
* REST API is used to call AccountService and LoanService.
* LoanWolvesService acts as a ochestrator and is used to call the different services in the right order. Services are dependant on each other and Ochestrator is waiting for repsonse before sending the next request.
* LoanService acts as a choreographer and is used to call LoanEvaluationService and LoanAmortizationService independent.
* The parts of the system that uses choreography has lower coupling and can still run even if some of the services go down.
* Camunda is used to send an email with the loan quote to the customer. 
* Loan Quote is based on loan types from the Legacy System and is converted to danish kroner through the API MoneyConvert that the Legacy System exsposes. The convertion is done through a Camel pipeline (LoanTypesGateway). 
* Loan types are saved locally and updated daily.

![](https://github.com/amalielandt/TheLoanWolves/blob/main/diagrams/new_setup.PNG)

