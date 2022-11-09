package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseStudyErrorCode implements ErrorCode {

  DUPLICATE_STUDY_ERROR("ITD_CSE_1", "Duplicate course study creation error",
      HttpStatus.BAD_REQUEST),
  USER_ROLE_ERROR("ITD_CSE_2", "Just regular users can study",
      HttpStatus.BAD_REQUEST),
  COURSE_WITH_USER_INFO_NOT_FOUND("ITD_CSE_3",
      "This user didn't started this course", HttpStatus.NOT_FOUND),
  END_DATE_TIME_ERROR("ITD_CSE_4", "End date couldn't be before start date",
      HttpStatus.BAD_REQUEST);


  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;
}
