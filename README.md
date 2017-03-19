# API REST Challenge
REST API Using HSQL for persistence and Hazelcast for in-memory statistic store

## build and run tests

```shell
mvn clean install
```

## start application

```shell
mvn spring-boot:run
```


## live demo on Heroku
`http://blabla`

### create transaction
```shell
curl -H "Content-Type: application/json" -X POST -d '{"timestamp":14555111561, "amount": 15.1}' http://blabla/transactions
```

### check statistic last 60 sec
```shell
curl -X GET http://blabla/statistics
```
