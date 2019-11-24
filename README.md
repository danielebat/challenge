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

- /account/create?userId={userId}&currency={currency}&amount={amount}
  - userId = id of the user requesting to add account
  - currency = account currency
  - amount = initial amount
- /account/delete?id={id}
  - id = id of the account to delete
- /account/deposit?id={id}&amount={amount}
  - id = id of the account receiving the deposit
  - amount = amount to deposit
- /account/withdraw?id={id}&amount={amount}
  - id = id of the account where to take the amount
  - amount = amount to withdraw
- /account/transfer?from={from}&to={to}&amount={amount}
  - from = id of the account from which take the amount
  - to = id of the account where to deposit money
  - amount = amount to transfer
- /account/list?id={id}
  - id = id of the user of which listing accounts

- /transaction/list?id={id}
  - id = id of the account of which listing transactions

