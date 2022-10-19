package com.rmr.dinosaurs.core.service.exceptions;

public class SurveyNotFoundException extends RuntimeException {

  public SurveyNotFoundException() {
    super();
  }

  public SurveyNotFoundException(String message) {
    super(message);
  }

  public SurveyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SurveyNotFoundException(Throwable cause) {
    super(cause);
  }

}
