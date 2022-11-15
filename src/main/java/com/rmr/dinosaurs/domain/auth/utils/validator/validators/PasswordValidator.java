package com.rmr.dinosaurs.domain.auth.utils.validator.validators;

import com.rmr.dinosaurs.domain.auth.utils.validator.PasswordConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements
    ConstraintValidator<PasswordConstraintValidator, String> {

  /**
   * Password must contain at least one digit [0-9]. Password must contain at least one lowercase
   * Latin character [a-z]. Password must contain at least one uppercase Latin character [A-Z].
   * Password must contain a length of at least 8 characters and a maximum of 20 characters.
   */
  private static final Pattern passwordPattern =
      Pattern.compile(
          "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*[0-9])"
              + "[A-ZА-Яa-zа-я0-9~!?@#$%^&*_\\-+()\\[\\]{}></\\\\|\"'.,:;]{8,20}$");

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return passwordPattern.matcher(s).matches();
  }

}
