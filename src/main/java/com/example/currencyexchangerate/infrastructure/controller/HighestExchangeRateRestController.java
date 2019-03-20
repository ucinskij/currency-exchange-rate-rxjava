package com.example.currencyexchangerate.infrastructure.controller;

import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRate;
import com.example.currencyexchangerate.gateway.ExchangeRateGateway;
import com.example.currencyexchangerate.highestrate.HighestCurrencyExchangeRateService;
import io.reactivex.Single;
import java.util.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HighestExchangeRateRestController {

  private final Logger logger = LoggerFactory.getLogger(HighestExchangeRateRestController.class);

  private final HighestCurrencyExchangeRateService service;

  HighestExchangeRateRestController(HighestCurrencyExchangeRateService service) {
    this.service = service;
  }

  @GetMapping("exchangerate/highest")
  Single<ExchangeRate> getHighestExchangeRate(@RequestParam("currencyFrom") String currencyFrom,
      @RequestParam("currencyTo") String currencyTo) {
    return service.getHighestExchangeRate(
        CurrencyConversion.from(parseToCurrency(currencyFrom)).to(parseToCurrency(currencyTo))
    );
  }

  private Currency parseToCurrency(String stringCurrency) {
    Currency currency;
    try {
      currency = Currency.getInstance(stringCurrency);
    } catch (IllegalArgumentException ex) {
      String message = String.format("Given currency: '%s' is invalid.", stringCurrency);
      logger.error(message, ex);
      throw new InvalidCurrencyException(message, ex);
    }
    return currency;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidCurrencyException.class)
  private ErrorMessage handleInvalidCurrencyException(InvalidCurrencyException ex){
    return new ErrorMessage(ex);
  }
}


