package com.example.currencyexchangerate.infrastructure.gateway;

import com.example.currencyexchangerate.gateway.CurrencyConversion;
import com.example.currencyexchangerate.gateway.ExchangeRateApiResponse;
import com.example.currencyexchangerate.gateway.ExchangeRateConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.reactivex.Single;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestOperations;

public class FreeCurrencyConverterConnection implements ExchangeRateConnection {

  @Value("${freecurrencyconverter.api.url}")
  private String apiUrl;
  @Value("${freecurrencyconverter.api.token}")
  private String token;

  private final RestOperations restOperations;

  public FreeCurrencyConverterConnection(RestOperations restOperations) {
    this.restOperations = restOperations;
  }

  @Override
  public Single<? extends ExchangeRateApiResponse> getApiResponse(
      CurrencyConversion currencyConversion) {
    String url = apiUrl + "?q={from}_{to}&compact=ultra&apiKey={token}";
    return Single.fromCallable(() -> callService(currencyConversion, url))
        .map(json -> getAmountFromJson(json, buildJsonFieldName(currencyConversion)))
        .map(FreeCurrencyConverterApiResponse::new);
  }

  private String callService(CurrencyConversion currencyConversion, String url) {
    return restOperations
        .getForObject(url, String.class, getRequestParams(currencyConversion));
  }

  private Map<String, String> getRequestParams(CurrencyConversion currencyConversion) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put("token", token);
    uriParams.put("from", currencyConversion.getFrom().toString());
    uriParams.put("to", currencyConversion.getTo().toString());
    return uriParams;
  }

  private BigDecimal getAmountFromJson(String json, String name) throws IOException {
    ObjectNode object = new ObjectMapper().readValue(json, ObjectNode.class);
    return object.get(name).decimalValue();
  }

  private String buildJsonFieldName(CurrencyConversion currencyConversion) {
    return currencyConversion.getFrom() + "_" + currencyConversion.getTo();
  }
}
