package com.example.currencyexchangerate.highestrate;

import static java.util.Comparator.comparing;
import static org.slf4j.LoggerFactory.getLogger;

import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRate;
import com.example.currencyexchangerate.gateway.ExchangeRateGateway;
import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighestCurrencyExchangeRateServiceImpl implements HighestCurrencyExchangeRateService {

  private final Logger logger = getLogger(HighestCurrencyExchangeRateServiceImpl.class);

  private final List<ExchangeRateGateway> exchangeRateGateways;

  public HighestCurrencyExchangeRateServiceImpl(List<ExchangeRateGateway> exchangeRateGateways) {
    this.exchangeRateGateways = exchangeRateGateways;
  }

  @Override
  public Single<ExchangeRate> getHighestExchangeRate(CurrencyConversion currencyConversion) {
    Observable<ExchangeRate> exchangeRateObservable = Observable
        .fromIterable(exchangeRateGateways)
        .flatMap(gateway -> gateway.getExchangeRate(currencyConversion).toObservable())
        .doOnNext(exchangeRate -> logger.info("Got rate: " + exchangeRate.getAmount()));
    return MathObservable
        .max(exchangeRateObservable, comparing(ExchangeRate::getAmount))
        .singleOrError();
  }
}
