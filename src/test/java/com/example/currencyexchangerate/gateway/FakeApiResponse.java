package com.example.currencyexchangerate.gateway;

import java.math.BigDecimal;

class FakeApiResponse implements ExchangeRateApiResponse {

  private final BigDecimal amount;

  FakeApiResponse(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public BigDecimal getAmount() {
    return amount;
  }
}
