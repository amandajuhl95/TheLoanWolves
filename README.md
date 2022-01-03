# The Loan Wolves
Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

#### Project Description

Loan Wolves is a swedish cooperative bank. Seated in Göteborg. They would like to open a department in Denmark. Their IT system is outdated so they would like to make a new one for the Danish department. Both systems should have the same functionality but the new one should be improved with performance in mind. The Loan Wolves will offer the same loan types in Denmark as they do in Sweden. Therefore, the new IT system should have access to the loan types that the bank offers. The loan types are stored in csv file(s) and are updated daily, by the current IT system. Doing the last couple of years, The Loan Wolves have received an increasing amount of loan requests in other currencies than SEK, therefore they have created an internal service to convert the amounts from international currencies. This service is separated from their main system. The service is accessed through a REST Api and can be used by the Danish IT-system.

#### Legacy System

The legacy system is built upon a 3-layer architecture. With a Presentation, business, and Data layer. It is used to handle loan quote requests, establish loans and amortization of loans. The system also handles accounts and transactions. To use the system, login is required, either as an employee or a customer. Whereas an employee has access to all functionalities, customers only have access to their own accounts and loans and can only request a loan quote on their own behalf.

All loan quotes offered to customers is saved in the database for later acceptance or rejection by the customer. When a loan quote is accepted, the loan is established, and the customer can amortize the loan. This is done by transferring money from the customer’s account. For each transfer the transaction is saved. For The Loan Wolves who are expanding, the amount of data stored will quickly increase, and can cause performance issues, due to the legacy system only using a single database for all data.

As of now the loan quote processing is very dependent on human interference since the system doesn’t really support automation. Therefore, the evaluation of the customer’s request is done manually by an employee. This means that when a customer requests a loan, their information and request is looked over by the employee, who decides if the customer is liable for the loan – based on certain criteria. If so, the employee will create a loan quote in the system based on the loan types and send an email with the loan quote to the customer. For the new system this process should be automated, to improve performance and customer satisfaction. 

The monolith connects to 3 components 
•	The currency converter, used to convert the requested loan amount to Swedish crones from other currency
•	The database used for storing user, account, and loan information
•	The CSV file containing the loan types that The Loan Wolves offers.
Both the CSV file and the currency converter should be used in the new system

Given the monolithic setup of the legacy system, other difficulties apply, such as:
•	Developing new functionality, causing inconsistent architecture with additional microservice.
•	Scaling the most used components of the system, without having to scale the entire system. This can affect the systems availability and limit the implementation of load balancing.
•	Limited use of different programming languages, due to the underlying framework

![image](https://user-images.githubusercontent.com/44894156/147925154-2a26ce54-ac0e-4a87-bd4e-f81bc70c761a.png)


![](https://github.com/amalielandt/TheLoanWolves/blob/main/diagrams/legacy_setup.PNG)

#### Setup the system 

1. Download the project 
2. In the project root, please run:
```
docker-compose up -d
```
3. Check in Docker that a container with h2 databases and kafka is running.
4. To open the databases, go to:
- localhost:8081 (to see user tables)
- localhost:8083 (to see loan tables)
- localhost:8085 (to see account tables)
- localhost:8089 (to see log table)

5. Open OffSet Explorer 2 and add a connection to the created docker container.
6. Make a docker container for Camunda, please run:
```
docker pull camunda/camunda-bpm-platform:latest
```
```
docker run -d --name camunda -p 8080:8080 camunda/camunda-bpm-platform:latest
```
 
8. Download Camunda Modeler.
9. In the menu tab click "file". Click "open file" and choose "diagram_1.bpmn", which can be found here: TheLoanWolves/LoanEvalutionService/Camunda/diagrams/
10. Deploy program by clicking: <img width="31" alt="Skærmbillede 2022-01-03 kl  11 59 21" src="https://user-images.githubusercontent.com/47500265/147923238-e0006a03-1c73-4d7a-8e1b-8497d4f086e7.png">

11. Give it a name, and select the two files, which can be found here: TheLoanWolves/LoanEvalutionService/Camunda/forms/

<img width="641" alt="Skærmbillede 2022-01-03 kl  11 58 53" src="https://user-images.githubusercontent.com/47500265/147923191-4337b054-285e-4306-98cc-77b26b741228.png">

12. Go to [Camunda Dashboard](http://localhost:8080/camunda/app/cockpit/default/#/dashboard).
13. Login using <b> demo </b> for both username and password
14. Click on "Process Definitions" 
15. Click on "SI_exam_Loan_picker_process"
16. The BPMN-diagram shows that processes are running and how far a task is in the flow.

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

#### Documentation of changes after hand-in

We have not added new functionalities to the system only improved performanced by changing the flow of the system, this includes:

1. Logging from all services saved in H2 database through our LoggerService, so we can keep track of our exceptions/errors.
2. Changed @pathvariables and @requestbody of endpoint no. 8. Before the entire user should be given, but as we already have the user saved in our H2 database, we only need to give the user-id.
3. Added an Enum with ACCEPTED, DECLINED or PENDING that is used to accept or decline a loan. We have therefore changed the @pathvariables of endpoint no. 10.
4. Endpoint no. 11 takes both amount and transaction type as body, and account id in path, so that both the transaction from the account and the amortization of the loan is executed
5. Changed port number of the h2 database of LoggingService.
6. Input validation for all endpoints at LoanWolvesService
7. Optimized errorhandling on all services
8. Optimized docker-compose file
9. Sender email changes




