package com.example.currencyexchangerate.highestrate;

import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRate;
import io.reactivex.Single;

public interface HighestCurrencyExchangeRateService {

  Single<ExchangeRate> getHighestExchangeRate(CurrencyConversion currencyConversion);
}
