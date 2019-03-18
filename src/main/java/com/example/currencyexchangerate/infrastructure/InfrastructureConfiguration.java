package com.example.currencyexchangerate.infrastructure;

import static java.util.Arrays.asList;

import com.example.currencyexchangerate.gateway.ApiCallTimeout;
import com.example.currencyexchangerate.gateway.ExchangeRateConnection;
import com.example.currencyexchangerate.gateway.ExchangeRateGateway;
import com.example.currencyexchangerate.gateway.ExchangeRateGatewayImpl;
import com.example.currencyexchangerate.highestrate.HighestCurrencyExchangeRateService;
import com.example.currencyexchangerate.highestrate.HighestCurrencyExchangeRateServiceImpl;
import com.example.currencyexchangerate.infrastructure.gateway.CurrencyDataFeedConnection;
import com.example.currencyexchangerate.infrastructure.gateway.FreeCurrencyConverterConnection;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


@Configuration
class InfrastructureConfiguration {

  @Bean
  HighestCurrencyExchangeRateService getHighestCurrencyExchangeRateService() {
    ApiCallTimeout timeout = new ApiCallTimeout(1, TimeUnit.SECONDS);
    return new HighestCurrencyExchangeRateServiceImpl(getExchangeRateGateways(timeout));
  }

  @Bean
  List<ExchangeRateGateway> getExchangeRateGateways(ApiCallTimeout timeout) {
    return asList(
        getCurrencyDataFeedGateway(timeout),
        getFreeCurrencyConverterGateway(timeout)
    );
  }

  @Bean
  ExchangeRateGateway getCurrencyDataFeedGateway(ApiCallTimeout timeout) {
    return new ExchangeRateGatewayImpl(getCurrencyDataFeedConnection(), Schedulers.io(), timeout);
  }

  @Bean
  ExchangeRateConnection getCurrencyDataFeedConnection() {
    return new CurrencyDataFeedConnection(getRestOperations());
  }

  @Bean
  ExchangeRateGateway getFreeCurrencyConverterGateway(ApiCallTimeout timeout) {
    return new ExchangeRateGatewayImpl(getFreeCurrencyConverterConnection(), Schedulers.io(),
        timeout);
  }

  @Bean
  ExchangeRateConnection getFreeCurrencyConverterConnection() {
    return new FreeCurrencyConverterConnection(getRestOperations());
  }

  @Bean
  RestOperations getRestOperations() {
    return new RestTemplate();
  }
}
