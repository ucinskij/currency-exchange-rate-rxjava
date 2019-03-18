package com.example.currencyexchangerate.gateway;

import java.util.concurrent.TimeUnit;

public class ApiCallTimeout {

  private final long timeout;
  private final TimeUnit timeUnit;

  public ApiCallTimeout(long timeout, TimeUnit timeUnit) {
    this.timeout = timeout;
    this.timeUnit = timeUnit;
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }
}
