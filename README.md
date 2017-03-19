# API REST Challenge
REST API using Spring Boot, HSQL for persistence and Hazelcast for in-memory statistic store

## build and run tests

```shell
mvn clean install
```

## start application

```shell
mvn spring-boot:run
```


## live demo on Heroku
`https://limitless-eyrie-62392.herokuapp.com/`
> NOTE: It is possible that app would be in idle state, so first request could take a little.

### create transaction
```shell
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{"amount": 8550.5, "timestamp": 1489960451087}' "https://limitless-eyrie-62392.herokuapp.com/transactions"
```

### check statistic last 60 sec
```shell
curl -X GET https://limitless-eyrie-62392.herokuapp.com/statistics
```
