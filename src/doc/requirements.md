# System Requirements Specification

## System Features

The Bank can have any number of customers and each customer can have multiple checking and savings accounts.
Account balance cannot be negative, transactions should fail if not enough money is available.
The Savings Account has a minimum balance should be 100, no debits or transfers can be made from this account if inactive.
The Customer can deposit or withdraw from the checking account or transfer money between checking and savings account
The Customer balance equals the sum of the balances of all the customer accounts.
The Customer can manage a list of recipients
The customer can transfer money from any of its accounts to any of its recipients
The Bank balance equals the sum of the balances of all customers.

## Functional Requirements
1. Bank should be able to contain any number of Customers
1. Customers should be able to have multiple checking accounts
1. Customers should be able to have multiple savings accounts
1. Checking account balance can not be negative
1. Savings account balance can not be lower than 100
1. Savings account requires transfer of at least minimum balance to become active
1. Customer should be able to deposit money in any of its checking accounts.
1. Customer should be able to withdraw money from any of its checking accounts, provided it has sufficient funds
1. Customer should be able to transfer money between any of its accounts
1. Customer should be able to check balance of any account
1. Customer should be able to check combined balance of all accounts
1. Bank should be able to check the combined balance of all customers

## Identifying Objects & Responsibilities

### Bank
Represents the Banking service, contains costumers

#### Responsibilities
* add a new customer
* get balance

#### Collaborators
* Customer

### Customer
Represents bank customers, contains accounts

#### Responsibilities
* add a new account
* deposit money on account
* withdraw money from account
* transfer between accounts
* get balance

#### Collaborators
* Account

### Account
Represents a Bank Account, contains money

#### Responsibilities
* credit
* debit
* get balance

## Design Decisions

* Inheritance used with generic Account class and specific CheckingAccount and SavingsAccount classes
* Composition and delegation used with AccountManager containing and performing all account related operations (less responsibility to customer)
* Account and Customer objects are stored in Map container for quick and convenient access
* AccountFactory decouples Account usage from Account creation logic
* Strategy pattern used for executing multiple bank operations and account transaction operations
* Console based user interface implemented using the propmt-view lib
* MVC architecture 
* Service layer for abstracting the domain model to the controllers

### MVC notes
 In classic MVC the Model notifies the view for changes, but it is also correct to
have the view reading the model, either directly or indirectly via the controller.
What can not happen is for the view to update the model directly. Model updates must always be done via the controller.

With the introduction of a service layer abstracting the domain model and providing a clean API to the controllers,
all views now fetch data from controllers only, making this setup more like an MVP

### Persistence notes
* One-To-Many bi-directional relationship between Customer and Account
