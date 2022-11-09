package com.rmr.dinosaurs.domain.auth.utils.validator.validators;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements
    ConstraintValidator<EmailConstraintValidator, String> {

  /**
   * RFC 5322 standard following regex pattern regex from
   * https://www.baeldung.com/java-email-validation-regex#strict-regular-expression-validation
   */
  protected static final Pattern emailPattern =
      Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
          + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return emailPattern.matcher(s).matches();
  }

}
