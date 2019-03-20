package com.example.currencyexchangerate.infrastructure.gateway;

import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRateApiResponse;
import com.example.currencyexchangerate.gateway.ExchangeRateConnection;
import io.reactivex.Single;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestOperations;

public class CurrencyDataFeedConnection implements ExchangeRateConnection {

  @Value("${currencydatafeed.api.url}")
  private String apiUrl;
  @Value("${currencydatafeed.api.token}")
  private String token;

  private final RestOperations restOperations;

  public CurrencyDataFeedConnection(RestOperations restOperations) {
    this.restOperations = restOperations;
  }

  @Override
  public Single<? extends ExchangeRateApiResponse> getApiResponse(
      CurrencyConversion currencyConversion) {
    String url = apiUrl + "?token={token}&currency={from}/{to}";
    return Single.fromCallable(() -> callService(currencyConversion, url));
  }

  private CurrencyDataFeedApiResponse callService(CurrencyConversion currencyConversion,
      String url) {
    return restOperations
        .getForEntity(url, CurrencyDataFeedApiResponse.class, getRequestParams(currencyConversion))
        .getBody();
  }

  private Map<String, String> getRequestParams(CurrencyConversion currencyConversion) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put("token", token);
    uriParams.put("from", currencyConversion.getFrom().toString());
    uriParams.put("to", currencyConversion.getTo().toString());
    return uriParams;
  }
}
