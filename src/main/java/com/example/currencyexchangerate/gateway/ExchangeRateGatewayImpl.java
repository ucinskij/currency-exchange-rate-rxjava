package com.example.currencyexchangerate.gateway;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeRateGatewayImpl implements ExchangeRateGateway {

  private final Logger logger = LoggerFactory.getLogger(ExchangeRateGatewayImpl.class);

  private final ExchangeRateConnection connection;
  private final Scheduler scheduler;
  private final ApiCallTimeout timeout;

  public ExchangeRateGatewayImpl(ExchangeRateConnection connection, Scheduler scheduler,
      ApiCallTimeout timeout) {
    this.connection = connection;
    this.scheduler = scheduler;
    this.timeout = timeout;
  }

  @Override
  public Single<ExchangeRate> getExchangeRate(CurrencyConversion currencyConversion) {
    return connection.getApiResponse(currencyConversion)
        .subscribeOn(scheduler)
        .map(response -> new ExchangeRate(response.getAmount(), currencyConversion))
        .timeout(timeout.getTimeout(), timeout.getTimeUnit(), scheduler)
        .doOnError(ex -> logger.error("Encountered an error while trying to retrieve an exchange rate.", ex))
        .onErrorReturnItem(new ExchangeRate(BigDecimal.ZERO, currencyConversion));
  }
}
