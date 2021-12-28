from os import error
from camunda.external_task.external_task import ExternalTask, TaskResult
from camunda.external_task.external_task_worker import ExternalTaskWorker
import threading
from numpy.lib.financial import rate
import pandas as pd
from datetime import date
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import json
from kafka import KafkaProducer

default_config = {
    "maxTasks": 1,
    "lockDuration": 10000,
    "asyncResponseTimeout": 5000,
    "retries": 3,
    "retryTimeout": 5000,
    "sleepSeconds": 30}

def get_loan_info(df, type, value):
    return df[df["type"]==type][value].iloc[0]

def liability(task: ExternalTask) -> TaskResult:

    print("Determening liability and selecting loan type for new client...")

    ## Cient information

    Age = task.get_variable("Age")
    FullName = task.get_variable("FullName")
    Email = task.get_variable("Email")
    CPR = task.get_variable("CPR")
    UserId = task.get_variable("UserId")
    Salary = task.get_variable("Salary")
    Amount = task.get_variable("Amount")
    Address = task.get_variable("Address")


    ## Loan type information
    loan_types = pd.read_csv("../../LoanTypesGateway/loantypes/dk_loantypes/loantypes.csv", delimiter=";")
    loan_types['min_limit'] = loan_types['min_limit'].astype('int')
    loan_types['max_limit'] = loan_types['max_limit'].astype('int')
    loan_types['interest_rate'] = loan_types['interest_rate'].astype('float') 
    loan_types['Duration'] = loan_types['Duration'].astype('int') 
    

    print("Age: ", Age, "FullName: ", FullName, "Salary: ", Salary, "Amount: ", Amount, "Address: ", Address)
    Liable, Fee, InterestRate, Lookover, Duration = False, 0, 0.0, False, 0
    bpmn_error = False

    if Amount == 0 or Salary == 0 or Age < 12 or FullName == None or CPR == None or UserId == None or Address == None:
        bpmn_error = True

    elif Amount < get_loan_info(loan_types, "Quick","max_limit"):
        print("Loan Accepted... first condition")
        Liable, Fee, InterestRate, Lookover, Duration = True, get_loan_info(loan_types, "Quick","fee"), get_loan_info(loan_types, "Quick","interest_rate"), False, get_loan_info(loan_types, "Quick","Duration")
    elif Amount < get_loan_info(loan_types, "Basic","max_limit") and Salary > 30000 and Age > 18:
        print("Loan Accepted... second condition")
        Liable, Fee, InterestRate, Lookover, Duration = True, get_loan_info(loan_types, "Basic","fee"), get_loan_info(loan_types, "Basic","interest_rate"), False, get_loan_info(loan_types, "Basic","Duration")
    elif Amount < get_loan_info(loan_types, "Asset","max_limit")  and Salary > 40000 and Age > 28:
        print("Loan Accepted... third condition")
        Liable, Fee, InterestRate, Lookover, Duration = True, get_loan_info(loan_types, "Asset","fee"), get_loan_info(loan_types, "Asset","interest_rate"), False, get_loan_info(loan_types, "Asset","Duration")
    elif Amount >= get_loan_info(loan_types, "Big","min_limit") and Salary > 45000 and Address != None and Age > 30:
        print("Loan Accepted... forth condition")
        Liable, Fee, InterestRate, Lookover, Duration = True, get_loan_info(loan_types, "Big","fee"), get_loan_info(loan_types, "Big","interest_rate"), True, get_loan_info(loan_types, "Big","Duration")

    if bpmn_error:
        producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'))
        producer.send('logging', {'serviceName': "loan_evaluation_worker", 'errorMessage' : 'one of the parameters was not provided. Remember to provide: Age, fullname, salary, amount and address'})
        return task.bpmn_error(error_code="Client_information_error", error_message="Client data error.. try restarting the process")

    print("The customers liable status was: ", Liable)
    return task.complete({
        "Age": Age, "FullName": FullName, "Email":Email, "CPR":CPR, "UserId":UserId, "Salary": float(Salary), "Amount": float(Amount), "Address": Address, "Liable": str(Liable), "Fee": float(Fee), "InterestRate": float(InterestRate), "Lookover": str(Lookover), "Duration": int(Duration)
    })

def send_email(task: ExternalTask) -> TaskResult:
    print("sending email to client...")

    FullName = task.get_variable("FullName")
    Email = task.get_variable("Email")
    Amount = task.get_variable("Amount")
    Address = task.get_variable("Address")
    CPR = task.get_variable("CPR")
    UserId = task.get_variable("UserId")
    Liable = task.get_variable("Liable")
    Fee = task.get_variable("Fee")
    InterestRate = task.get_variable("InterestRate")
    Duration = task.get_variable("Duration")

    if Liable == "True":

        today = str(date.today())
        with open ("loan_agreement.txt", "r",encoding='utf8') as contract:
            mail_content=contract.readlines()

        mail_content = "".join(mail_content)
        Subject = "Your request has been accepted"
        mail_content = mail_content.format(today,FullName, Address, Amount, Duration,Amount, InterestRate, Fee)

    else:
        Subject = "Your request has been declined"
        mail_content = """
        Dear {},
        I am sad to inform you that your request for a loan of {} DKK has been decliend.
        We hope you will come back once you make more money and need us again
        Best reguards
        Loan Wolfs
        """.format(FullName, Amount)

    try:
        #The mail addresses and password
        sender = "theloanwolvesdk@gmail.com"
        reciver = Email

        sender_address = sender
        sender_pass = 'chokobanan1!'
        receiver_address = reciver
        #Setup the MIME
        message = MIMEMultipart()
        message['From'] = sender_address
        message['To'] = receiver_address
        message['Subject'] = Subject
        #The body and the attachments for the mail
        message.attach(MIMEText(mail_content, 'plain'))
        #Create SMTP session for sending the mail
        session = smtplib.SMTP('smtp.gmail.com', 587) #use gmail with port
        session.starttls() #enable security
        session.login(sender_address, sender_pass) #login with mail_id and password
        text = message.as_string()
        session.sendmail(sender_address, receiver_address, text)
        session.quit()
        print('Mail Sent')
    except smtplib.SMTPAuthenticationError as e:
        producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'))
        producer.send('logging', {'serviceName': "loan_evaluation_worker", 'errorMessage' : 'Could not connect to the mail theloanwolvesdk@gmail.com. See full error:' + str(e.smtp_error)})
        raise Exception("Could not connect to the mail theloanwolvesdk@gmail.com. See full error: ", e.smtp_error)


    return task.complete({ "FullName": FullName, "Email":Email, "CPR":CPR, "UserId":UserId, "Amount": float(Amount), "Fee": float(Fee), "InterestRate": float(InterestRate), "Duration":int(Duration)})

def save_loan_quote(task: ExternalTask) -> TaskResult:
    
    Amount = task.get_variable("Amount")
    CPR = task.get_variable("CPR")
    UserId = task.get_variable("UserId")
    Fee = task.get_variable("Fee")
    InterestRate = task.get_variable("InterestRate")
    Duration = task.get_variable("Duration")

    print(Amount,CPR,UserId,Fee,InterestRate,Duration)

    try:
        producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'))
        producer.send('save-loan-quote', {'amount': Amount,'cpr':CPR ,'userId':UserId ,'fee':Fee ,'interestRate':InterestRate ,'duration': Duration})
        return task.complete()

    except Exception  as e:
        raise ConnectionError("Could not connect to Kafka, check if it is running.")





def handle_liability():
    ExternalTaskWorker(worker_id="1", config=default_config).subscribe(
        "Liable", liability)


def handle_send_email():
    ExternalTaskWorker(worker_id="2", config=default_config).subscribe(
        "send_email", send_email)


def handle_save_loan_quote():
    ExternalTaskWorker(worker_id="3", config=default_config).subscribe(
        "save_loan_quote", save_loan_quote)


if __name__ == '__main__':
    print("Starting Handelers...")
    t1 = threading.Thread(target=handle_liability)
    t1.start()
    t2 = threading.Thread(target=handle_send_email)
    t2.start()
    t3 = threading.Thread(target=handle_save_loan_quote)
    t3.start()
    print("Handelers started...")



