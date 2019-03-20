package com.example.currencyexchangerate.highestrate;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.example.currencyexchangerate.gateway.ApiCallTimeout;
import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRate;
import com.example.currencyexchangerate.gateway.ExchangeRateConnectionStub;
import com.example.currencyexchangerate.gateway.ExchangeRateGatewayImpl;
import com.example.currencyexchangerate.gateway.SlowExchangeRateConnectionStub;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HighestCurrencyExchangeRateServiceTest {

  private final Currency PLN = Currency.getInstance("PLN");
  private final Currency USD = Currency.getInstance("USD");
  private final BigDecimal HIGHEST_RATE = new BigDecimal("3.55");
  private final BigDecimal LOWEST_RATE = new BigDecimal("3.15");

  private ApiCallTimeout timeout = ApiCallTimeout.MILLISECONDS(500);
  private TestScheduler testScheduler;

  @BeforeEach
  void setUp() {
    testScheduler = new TestScheduler();
  }

  @Test
  void getHighestExchangeRate() {
    CurrencyConversion currencyConversion = CurrencyConversion.from(USD).to(PLN);
    HighestCurrencyExchangeRateService service = getHighestCurrencyExchangeRateService();

    TestObserver<ExchangeRate> exchangeRateObserver = service.getHighestExchangeRate(currencyConversion).test();

    testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);
    exchangeRateObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(1)
        .assertValue(this::isExchangeRateEqual);
  }

  private HighestCurrencyExchangeRateService getHighestCurrencyExchangeRateService() {
    return new HighestCurrencyExchangeRateServiceImpl(asList(
        new ExchangeRateGatewayImpl(new ExchangeRateConnectionStub(HIGHEST_RATE), testScheduler, timeout),
        new ExchangeRateGatewayImpl(new ExchangeRateConnectionStub(LOWEST_RATE), testScheduler, timeout)
    ));
  }

  private boolean isExchangeRateEqual(ExchangeRate exchangeRate) {
    return exchangeRate.getAmount().compareTo(HIGHEST_RATE) == 0;
  }

  @Test
  void getHighestExchangeRate_slowResponseReachesTimeout_returnsExchangeRateEqualZero() {
    CurrencyConversion currencyConversion = CurrencyConversion.from(USD).to(PLN);
    HighestCurrencyExchangeRateService service = getSlowHighestCurrencyExchangeRateService();

    TestObserver<ExchangeRate> exchangeRateObserver = service.getHighestExchangeRate(currencyConversion).test();

    testScheduler.advanceTimeBy(timeout.getTimeout() + 100, TimeUnit.MILLISECONDS);
    exchangeRateObserver
        .assertComplete()
        .assertValueCount(1)
        .assertValue(exchangeRate -> exchangeRate.getAmount().equals(BigDecimal.ZERO))
        .assertValue(exchangeRate -> exchangeRate.getCurrencyConversion().equals(currencyConversion));
  }

  private HighestCurrencyExchangeRateService getSlowHighestCurrencyExchangeRateService() {
    return new HighestCurrencyExchangeRateServiceImpl(asList(
        new ExchangeRateGatewayImpl(new SlowExchangeRateConnectionStub(HIGHEST_RATE), testScheduler, timeout),
        new ExchangeRateGatewayImpl(new SlowExchangeRateConnectionStub(LOWEST_RATE), testScheduler, timeout)
    ));
  }
}
