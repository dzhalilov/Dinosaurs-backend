package com.rmr.dinosaurs.core.service.exceptions;

public class CourseProviderNotFoundException extends RuntimeException {

  public CourseProviderNotFoundException() {
    super();
  }

  public CourseProviderNotFoundException(String message) {
    super(message);
  }

  public CourseProviderNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public CourseProviderNotFoundException(Throwable cause) {
    super(cause);
  }

}
