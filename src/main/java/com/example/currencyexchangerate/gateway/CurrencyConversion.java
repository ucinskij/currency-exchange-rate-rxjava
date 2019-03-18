package com.example.currencyexchangerate.gateway;

import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

public class CurrencyConversion implements Serializable {

  private final Currency from;
  private final Currency to;

  public CurrencyConversion(Currency from, Currency to) {
    this.from = from;
    this.to = to;
  }

  public Currency getFrom() {
    return from;
  }

  public Currency getTo() {
    return to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyConversion that = (CurrencyConversion) o;
    return from.equals(that.from) &&
        to.equals(that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }
}
