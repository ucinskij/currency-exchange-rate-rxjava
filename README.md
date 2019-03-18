Introduction:

This project is a playground for RxJava in order to learn some of its basics. Starting off with a simple Spring Boot app that requests currency exchange rate  from multiple services and returns only one, that has the highest exchange rate. 
The project implements Gateway pattern [Fowler].

Running the project:
You can run it from IntelliJ or command line like a classic Spring Boot app. By default it's available on port `8080`

Example endpoint:
`http://localhost:8080/exchangerate/highest?currencyFrom=USD&currencyTo=PLN`

TODO:
- Error handling - in case there's no token, NPE is being thrown
- In case of API call failure, get data from cache (`Observable.onErrorResumeNext()` could be useful)
- With cache in place, `HighestCurrencyExchangeRateService`'s behavior should change. In case all the services fail, a rate from cache should be returned (`HighestCurrencyExchangeRateServiceTest.getHighestExchangeRate_slowResponseEndsWithTimeout()`)
- Integration tests
- Maven profile for integration tests
