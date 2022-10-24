package com.rmr.dinosaurs.core.exception.errorcode;

import com.rmr.dinosaurs.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserInfoErrorCode implements ErrorCode {

  USER_INFO_NOT_FOUND("ITD_UIEC_1", "No such user profile found", HttpStatus.NOT_FOUND),
  NO_PERMISSIONS_TO_EDIT("ITD_UIEC_2", "Current user can't edit the profile not belongs to",
      HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}
