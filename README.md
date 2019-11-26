# Challenge

To create the executable jar, please run the following command in command line
```
mvn clean install
```
in the project folder (Java 8 is required). In target folder, there will be a file called system-0.0.1-release.jar. To run it, please run from command line
```
java -jar system-0.0.1-release.jar
```
## HTTP requests

- [CREATE A BANK ACCOUNT]
  - Request
    - /account/create?userId={userId}&currency={currency}&amount={amount}
  - Parameters
    - userId = id of the user requesting to add account
    - currency = account currency
    - amount = initial amount
- [DELETE A BANK ACCOUNT]
  - Request
    - /account/delete?id={id}
  - Parameters
    - id = id of the account to delete
- [DEPOSIT AN AMOUNT OF MONEY ON A BANK ACCOUNT]
  - Request
    - /account/deposit?id={id}&amount={amount}
  - Parameters
    - id = id of the account receiving the deposit
    - amount = amount to deposit
- [WITHDRAW AN AMOUNT OF MONEY FROM A BANK ACCOUNT]
  - Request
    - /account/withdraw?id={id}&amount={amount}
  - Parameters
    - id = id of the account where to take the amount
    - amount = amount to withdraw
- [TRANSFER AN AMOUNT OF MONEY FROM A BANK ACCOUNT TO ANOTHER]
  - Request
    - /account/transfer?from={from}&to={to}&amount={amount}
  - Parameters
    - from = id of the account from where to take the amount
    - to = id of the account where to deposit money
    - amount = amount to transfer
- [LIST ALL THE ACCOUNTS OF A USER]
  - Request
    - /account/list?id={id}
  - Parameters
    - id = id of the user of which listing accounts
- [LIST ALL THE TRANSACTIONS PROCESSED FOR AN ACCOUNT]
  - Request
    - /transaction/list?id={id}
  - Parameters
    - id = id of the account of which listing transactions

