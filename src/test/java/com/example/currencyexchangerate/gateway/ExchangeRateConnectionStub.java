package com.example.currencyexchangerate.gateway;

import io.reactivex.Single;
import java.math.BigDecimal;

public class ExchangeRateConnectionStub implements ExchangeRateConnection {

  private final BigDecimal expectedAmount;

  public ExchangeRateConnectionStub(BigDecimal expectedAmount) {
    this.expectedAmount = expectedAmount;
  }

  @Override
  public Single<? extends ExchangeRateApiResponse> getApiResponse(
      CurrencyConversion currencyConversion) {
    return Single.just(new FakeApiResponse(expectedAmount));
  }
}
