package com.rmr.dinosaurs.domain.userinfo.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserInfoErrorCode implements ErrorCode {

  USER_INFO_NOT_FOUND("ITD_UIEC_1", "No such user profile found", HttpStatus.NOT_FOUND),
  NO_PERMISSIONS_TO_EDIT("ITD_UIEC_2", "Current user can't edit the profile not beLongs to",
      HttpStatus.BAD_REQUEST),
  NO_PERMISSIONS_TO_DELETE("ITD_UIEC_3", "Only regular user can delete profile",
      HttpStatus.FORBIDDEN);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}
