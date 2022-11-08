package com.rmr.dinosaurs.domain.notification.email.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum MailErrorCode implements ErrorCode {

  FAILED_EMAIL_SEND("ITD_MEC_1", "Can't send an email", HttpStatus.BAD_REQUEST),
  INCORRECT_EMAIL_DATA("ITD_MEC_2", "Message is null or recipients list is empty",
      HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}
