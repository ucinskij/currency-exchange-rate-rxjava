package com.example.currencyexchangerate.gateway;

import io.reactivex.Single;

public interface ExchangeRateConnection {

  Single<? extends ExchangeRateApiResponse> getApiResponse(CurrencyConversion currencyConversion);
}
