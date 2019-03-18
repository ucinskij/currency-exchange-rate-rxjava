package com.example.currencyexchangerate.infrastructure.controller;

class ErrorMessage {

  private final String message;

  ErrorMessage(Exception e) {
    this.message = e.getLocalizedMessage();
  }

  public String getMessage() {
    return message;
  }
}
