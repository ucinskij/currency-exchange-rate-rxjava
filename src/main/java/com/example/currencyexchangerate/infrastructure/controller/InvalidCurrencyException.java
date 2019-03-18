package com.example.currencyexchangerate.infrastructure.controller;

class InvalidCurrencyException extends RuntimeException {

  InvalidCurrencyException(String message, Throwable cause) {
    super(message, cause);
  }
}
