package com.example.currencyexchangerate.infrastructure.gateway;

import com.example.currencyexchangerate.gateway.ExchangeRateApiResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

class CurrencyDataFeedApiResponse implements ExchangeRateApiResponse {

  private final List<Currency> currencies;

  @JsonCreator
  CurrencyDataFeedApiResponse(@JsonProperty("currency") List<Currency> currencies) {
    this.currencies = currencies;
  }

  @Override
  public BigDecimal getAmount() {
    return currencies.get(0).getValue();
  }

  static class Currency {
    private final BigDecimal value;

    @JsonCreator
    Currency(@JsonProperty("value") BigDecimal value) {
      this.value = value;
    }

    BigDecimal getValue() {
      return value;
    }
  }
}
