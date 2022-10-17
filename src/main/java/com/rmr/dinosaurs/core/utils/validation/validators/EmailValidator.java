package com.rmr.dinosaurs.core.utils.validation.validators;

import com.rmr.dinosaurs.core.utils.validation.EmailConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements
    ConstraintValidator<EmailConstraintValidator, String> {

  /**
   * RFC 5322 standard following regex pattern
   */
  protected static final Pattern emailPattern =
      Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return emailPattern.matcher(s).matches();
  }

}
