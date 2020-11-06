# OpenPayd Technical Challenge

This project has been created for technical assessment to be considered during interview processes.

This project has 3 main packages : 

- `exhangerate`: Contains all of the classes that will be used to query the FX rates from a service provider (RatesAPI - https://ratesapi.io/).
- `conversion`: Contains all of the classes which handles conversion operations between currencies and listing the existing transactions. To convert the amounts, it is using "exchangerate" package. 
- `common`: Contains the config, response and exception classes that are being used commonly by any package. 

The project is using in memory DB to store the transactions logging errors to the console.

# Requirements

- Java 1.8
- Maven 3.5+

# Build and Run

First of all, make sure that you are in the main folder of the project.

To build the application, run the command below : 

```
mvn clean install
```

Then, to run the application, run the command below : 

```
java -jar target/conversion-0.0.1-SNAPSHOT.jar
```

# Usage

There are 3 endpoints : 

- ### 1 - Query FX Rates

`GET /api/exchangerate/getrate/{sourceCurrency}/{targetCurrency}`

Returns the FX rate for given currency pairs.

##### Sample

Request : 

`GET http://localhost:8080/api/exchangerate/getrate/USD/TRY`

Response : 

```json
{
     "data": {
         "exchangeRate": {
             "sourceCurrency": "USD",
             "targetCurrency": "TRY",
             "exchangeRate": 8.4335723323
         }
     }
 }
 ```

- ### 2 - Converting the amount from source to target currency

`POST /api/conversion/convert`

Converts the amount to target currency and inserts the transaction data to DB.

##### Sample

Request : 

`POST http://localhost:8080/api/conversion/convert`

Request Body : 

```json
{
	"sourceCurrency" : "USD",
	"targetCurrency" : "TRY",
	"sourceAmount" : 100
}
 ```

Response : 

```json
{
    "data": {
        "conversion": {
            "id": 2,
            "transactionDate": "2020-11-06",
            "sourceCurrency": "USD",
            "targetCurrency": "TRY",
            "sourceAmount": 100,
            "targetAmount": 843.3572332300,
            "exchangeRate": 8.4335723323
        }
    }
}
 ```

- ### 3 - List Conversion Transactions

`GET /api/conversion/list?transactionId={transactionId}&transactionDate={transactionDate}&page={pageNumber}&size={pageSize}`

Returns the FX rate for given currency pairs.
Four parameters in the url are optional. Default page number is 0 and size is 10.
At least one of transaction id or transaction date must be defined in the url.

##### Sample

Request : 

`GET http://localhost:8080/api/conversion/list?transactionDate=2020-11-06`

Response : 

```json
{
    "data": {
        "conversions": [
            {
                "id": 1,
                "transactionDate": "2020-11-06",
                "sourceCurrency": "EUR",
                "targetCurrency": "TRY",
                "sourceAmount": 100.00,
                "targetAmount": 1014.89,
                "exchangeRate": 10.15
            },
            {
                "id": 2,
                "transactionDate": "2020-11-06",
                "sourceCurrency": "USD",
                "targetCurrency": "TRY",
                "sourceAmount": 100.00,
                "targetAmount": 843.36,
                "exchangeRate": 8.43
            }
        ]
    }
}
 ```



