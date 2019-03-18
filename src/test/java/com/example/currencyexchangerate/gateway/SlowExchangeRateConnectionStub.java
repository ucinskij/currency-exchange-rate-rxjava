package com.example.currencyexchangerate.gateway;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.reactivex.Single;
import java.math.BigDecimal;

public class SlowExchangeRateConnectionStub implements ExchangeRateConnection {

  private final BigDecimal amount;

  public SlowExchangeRateConnectionStub(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public Single<? extends ExchangeRateApiResponse> getApiResponse(
      CurrencyConversion currencyConversion) {
    return Single
        .just(new FakeApiResponse(amount))
        .delay(2, SECONDS);
  }
}
