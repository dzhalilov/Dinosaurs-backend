package com.rmr.dinosaurs.domain.core.utils.validator.validators;


import com.rmr.dinosaurs.domain.core.utils.validator.UrlConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements
    ConstraintValidator<UrlConstraintValidator, String> {

  private static final Pattern URL_PATTERN =
      Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Zа-яА-Я0-9@:%._\\+~#=]{1,235}\\."
          + "[a-zA-Zа-яА-Я0-9()]{1,6}\\b([-a-zA-Zа-яА-Я0-9()@:%_\\+.~#?&//=]*)");

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return URL_PATTERN.matcher(s).matches();
  }

}
