package com.rmr.dinosaurs.domain.auth.utils.validator.validators;

import com.rmr.dinosaurs.domain.auth.utils.validator.UserNameAndSurnameConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameAndSurnameValidator implements
    ConstraintValidator<UserNameAndSurnameConstraintValidator, String> {

  private static final Pattern nameAndSurnamePattern =
      Pattern.compile(
          "(^[A-Z][a-z]{0,255})|(^[А-Я][а-я]{0,255})");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return nameAndSurnamePattern.matcher(value).matches();
  }
}
