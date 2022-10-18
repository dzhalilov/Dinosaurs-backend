package com.rmr.dinosaurs.core.service.exceptions;

public class NegativePageNumberException extends RuntimeException {

  public NegativePageNumberException() {
    super();
  }

  public NegativePageNumberException(String message) {
    super(message);
  }

  public NegativePageNumberException(String message, Throwable cause) {
    super(message, cause);
  }

  public NegativePageNumberException(Throwable cause) {
    super(cause);
  }

}
