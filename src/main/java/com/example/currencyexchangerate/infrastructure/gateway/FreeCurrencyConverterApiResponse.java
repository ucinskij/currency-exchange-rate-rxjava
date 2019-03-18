package com.example.currencyexchangerate.infrastructure.gateway;

import com.example.currencyexchangerate.gateway.ExchangeRateApiResponse;
import java.math.BigDecimal;

class FreeCurrencyConverterApiResponse implements ExchangeRateApiResponse {

  private final BigDecimal amount;

  FreeCurrencyConverterApiResponse(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public BigDecimal getAmount() {
    return amount;
  }
}
