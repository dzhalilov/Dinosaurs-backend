package com.rmr.dinosaurs.domain.auth.utils.validator.validators;

import com.rmr.dinosaurs.domain.auth.utils.validator.UserNameAndSurnameConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameAndSurnameValidator implements
    ConstraintValidator<UserNameAndSurnameConstraintValidator, String> {

  private static final Pattern nameAndSurnamePattern =
      Pattern.compile(
          "((^[A-Z]([a-z]{1,48}([- ]{1}[A-Z]{1})?([a-z]{1,45})?))|"
              + "(^[А-Я]([а-я]{1,48}([- ]{1}[А-Я]{1})?([а-я]{1,45})?))){1,49}");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return nameAndSurnamePattern.matcher(value).matches();
  }
}
