package com.rmr.dinosaurs.domain.auth.utils.validator.validators;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements
    ConstraintValidator<EmailConstraintValidator, String> {

  protected static final Pattern emailPattern =
      Pattern.compile("^(?![.])([A-Za-z0-9_\\-.](?!.*\\.\\.)){1,64}@"
          + "([^\\-. ][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*.*(?<!-))(\\.[A-Za-z]{2,})$");

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return emailPattern.matcher(s).matches();
  }

}
