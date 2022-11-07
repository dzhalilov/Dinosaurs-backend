package com.rmr.dinosaurs.domain.core.utils.validator;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.rmr.dinosaurs.domain.core.utils.validator.validators.UrlValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@Target({PARAMETER, CONSTRUCTOR, METHOD, FIELD, RECORD_COMPONENT})
@Retention(RUNTIME)
@NotNull
@Constraint(validatedBy = UrlValidator.class)
@Documented
public @interface UrlConstraintValidator {

  String message() default "Invalid url value";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
