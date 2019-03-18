package com.example.currencyexchangerate.gateway;

import io.reactivex.Single;

public interface ExchangeRateGateway {

  Single<ExchangeRate> getExchangeRate(CurrencyConversion currencyConversion);
}
