package com.example.currencyexchangerate.gateway;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExchangeRateGatewayTest {

  private final Currency PLN = Currency.getInstance("PLN");
  private final Currency USD = Currency.getInstance("USD");
  private final BigDecimal EXPECTED_AMOUNT_FROM_API = new BigDecimal("3.55");

  private ApiCallTimeout timeout = ApiCallTimeout.MILLISECONDS(500);
  private TestScheduler testScheduler;

  @BeforeEach
  void setUp() {
    testScheduler = new TestScheduler();
  }

  @Test
  void getExchangeRate() {
    CurrencyConversion currencyConversion = new CurrencyConversion(USD, PLN);
    ExchangeRateGateway gateway = getGateway();

    TestObserver<ExchangeRate> exchangeRateObserver = gateway.getExchangeRate(currencyConversion).test();

    testScheduler.advanceTimeBy(50, MILLISECONDS);
    exchangeRateObserver
        .assertComplete()
        .assertValueCount(1)
        .assertValue(exchangeRate -> exchangeRate.getAmount().equals(EXPECTED_AMOUNT_FROM_API))
        .assertValue(exchangeRate -> exchangeRate.getCurrencyConversion().equals(currencyConversion));
  }

  private ExchangeRateGateway getGateway() {
    ExchangeRateConnection connection = new ExchangeRateConnectionStub(EXPECTED_AMOUNT_FROM_API);
    return new ExchangeRateGatewayImpl(connection, testScheduler, timeout);
  }

  @Test
  void getExchangeRate_slowResponseReachesTimeout_returnsExchangeRateEqualZero() {
    CurrencyConversion currencyConversion = new CurrencyConversion(USD, PLN);
    ExchangeRateGateway gateway = getSlowGateway();

    TestObserver<ExchangeRate> exchangeRateObserver = gateway.getExchangeRate(currencyConversion).test();

    testScheduler.advanceTimeBy(timeout.getTimeout() + 100, MILLISECONDS);
    exchangeRateObserver
        .assertComplete()
        .assertValueCount(1)
        .assertValue(exchangeRate -> exchangeRate.getAmount().equals(BigDecimal.ZERO))
        .assertValue(exchangeRate -> exchangeRate.getCurrencyConversion().equals(currencyConversion));
  }

  private ExchangeRateGateway getSlowGateway() {
    ExchangeRateConnection connection = new SlowExchangeRateConnectionStub(EXPECTED_AMOUNT_FROM_API);
    return new ExchangeRateGatewayImpl(connection, testScheduler, timeout);
  }
}
