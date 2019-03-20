package com.example.currencyexchangerate.gateway;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

public class ApiCallTimeout {

  private final long timeout;
  private final TimeUnit timeUnit;

  private ApiCallTimeout(long timeout, TimeUnit timeUnit) {
    this.timeout = timeout;
    this.timeUnit = timeUnit;
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  public static ApiCallTimeout MILLISECONDS(long timeout) {
    return new ApiCallTimeout(timeout, MILLISECONDS);
  }

  public static ApiCallTimeout SECONDS(long timeout) {
    return new ApiCallTimeout(timeout, SECONDS);
  }
}
