# Consumer Calculator Project

Consumer Calculator Project is an application that will process metering data coming from customers.

### Usefull concepts:
- Meter: is a device that is measuring the amount of gas or electricity being used within a house. It's
just a counter, so it will be a number that is increasing along time.
-  Meter reading: It's the number that the Meter is showing at a specific date andtime.
- Consumption: It's the difference, between two given date/times, of meter readings. Example: If
the meter reading on 2016/01/15 is 120 and the meter reading on 2016/02/15 is 150, then the
consumption between 2016/01/15 and 2016/02/15 is 30. Could be KWh, m3, but the unit of
measure is not relevant for this exercise.
- Profile: It's a collection of ratios [0..1], called Fractions, for every calendar month. It represents
how the consumption of a given year is distributed among months, so all the fractions for the 12
months must sum up 1. For example, for a house in the Netherlands it would be normal to have
higher ratios during winter than during summer, given that the energy consumed will be higher
because of heating.

### Setup And Run:

#### Setup
- setup db
```
- docker pull mariadb
- docker run --name meterdb -e MYSQL_DATABASE=meter -e MYSQL_USER=root -e MYSQL_ROOT_PASSWORD=root  -d -p 3306:3306  meterdb
- you must create a db which name is meter after that
- for the first time : you must change the spring.jpa.hibernate.ddl-auto properties
  under the application.properties as 'create' after the first time, if you want
  do not want to create when you start everytime you should update as 'update'
  to the parameter
```
#### Run
```
- you must have maven in your machine to run the application
- mvn spring-boot:run can run the applicaition also, you can run the application
  from com.servicehouse.metricCalculator.MetricProcessorApplication class which is
  main for the application
```


#### Request sample

- you can use the curl command to add a fraction like the following command
```
curl -X POST "http://localhost:8080/profile/fraction" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"profileFractionList\": [ { \"fractionPercentage\": 0.1, \"month\": \"JAN\", \"name\": \"A\" }, { \"fractionPercentage\": 0.2, \"month\": \"FEB\", \"name\": \"A\" }, { \"fractionPercentage\": 0.1, \"month\": \"MAR\", \"name\": \"A\" }, { \"fractionPercentage\": 0.1, \"month\": \"APR\", \"name\": \"A\" }, { \"fractionPercentage\": 0.1, \"month\": \"MAY\", \"name\": \"A\" }, { \"fractionPercentage\": 0.1, \"month\": \"JUNE\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"JULY\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"AUG\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"SEPT\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"OCT\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"NOV\", \"name\": \"A\" }, { \"fractionPercentage\": 0.05, \"month\": \"DEC\", \"name\": \"A\" } ]}"
```

- you can use http://localhost:8080/swagger-ui.html#/ to send the fraction list with using "try it out" button
```

{
  "profileFractionList": [
    {
      "fractionPercentage": 0.1,
      "month": "JAN",
      "name": "A"
    },
    {
      "fractionPercentage": 0.2,
      "month": "FEB",
      "name": "A"
    },
    {
      "fractionPercentage": 0.1,
      "month": "MAR",
      "name": "A"
    },
    {
      "fractionPercentage": 0.1,
      "month": "APR",
      "name": "A"
    },
    {
      "fractionPercentage": 0.1,
      "month": "MAY",
      "name": "A"
    },
    {
      "fractionPercentage": 0.1,
      "month": "JUNE",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "JULY",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "AUG",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "SEPT",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "OCT",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "NOV",
      "name": "A"
    },
    {
      "fractionPercentage": 0.05,
      "month": "DEC",
      "name": "A"
    }
  ]
}

```
- you can use http://localhost:8080/swagger-ui.html#/ to send the meter list with using "try it out" button

```
{
  "profileMeterList": [
    {
      "meterId": "0001",
      "meterReading": 10,
      "month": "JAN",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 30,
      "month": "FEB",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 40,
      "month": "MAR",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 50,
      "month": "APR",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 60,
      "month": "MAY",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 70,
      "month": "JUNE",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 75,
      "month": "JULY",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 80,
      "month": "AUG",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 85,
      "month": "SEPT",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 95,
      "month": "NOV",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 90,
      "month": "OCT",
      "name": "A"
    },
    {
      "meterId": "0001",
      "meterReading": 100,
      "month": "DEC",
      "name": "A"
    }
  ]
}
```

- you can use the curl command to get a meters according to its Month and MeterId like the following command
```
curl -X POST "http://localhost:8080/profile/meter" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"profileMeterList\": [ { \"meterId\": \"0001\", \"meterReading\": 10, \"month\": \"JAN\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 30, \"month\": \"FEB\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 40, \"month\": \"MAR\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 50, \"month\": \"APR\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 60, \"month\": \"MAY\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 70, \"month\": \"JUNE\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 75, \"month\": \"JULY\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 80, \"month\": \"AUG\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 85, \"month\": \"SEPT\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 95, \"month\": \"NOV\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 90, \"month\": \"OCT\", \"name\": \"A\" }, { \"meterId\": \"0001\", \"meterReading\": 100, \"month\": \"DEC\", \"name\": \"A\" } ]}"
```

- you can use curl command to get consumption for that month
```
curl -X GET "http://localhost:8080/profile/0001/MAR" -H "accept: */*"
```


- you can use curl command to post a fraction csv file place to parse it
```
curl -X POST "http://localhost:8080/profile/fraction/mock-path" -H "accept: */*"
```

- you can use curl command to post a meter csv file place to parse it
```
curl -X POST "http://localhost:8080/profile/meter/mock-path" -H "accept: */*"
```

- on status codes
```http
201 – in case of success
400 – if the JSON is invalid
404 – if wanted meter consumption does not exist
```

