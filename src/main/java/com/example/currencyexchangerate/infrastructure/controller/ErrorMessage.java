package com.example.currencyexchangerate.infrastructure.controller;

import org.springframework.http.HttpStatus;

class ErrorMessage {

  private final int status;
  private final String message;

  ErrorMessage(HttpStatus httpStatus, Exception e) {
    this.status = httpStatus.value();
    this.message = e.getLocalizedMessage();
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
