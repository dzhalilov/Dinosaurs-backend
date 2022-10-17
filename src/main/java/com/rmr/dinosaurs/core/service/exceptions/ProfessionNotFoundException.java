package com.rmr.dinosaurs.core.service.exceptions;

public class ProfessionNotFoundException extends RuntimeException {

  public ProfessionNotFoundException() {
    super();
  }

  public ProfessionNotFoundException(String message) {
    super(message);
  }

  public ProfessionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProfessionNotFoundException(Throwable cause) {
    super(cause);
  }

}
