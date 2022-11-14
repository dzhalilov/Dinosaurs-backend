package com.rmr.dinosaurs.domain.auth.model.requests;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import com.rmr.dinosaurs.domain.auth.utils.validator.PasswordConstraintValidator;
import com.rmr.dinosaurs.domain.auth.utils.validator.UserNameAndSurnameConstraintValidator;
import javax.validation.constraints.NotNull;

public record SignupRequest(
    @EmailConstraintValidator String email,
    @PasswordConstraintValidator String password,
    @UserNameAndSurnameConstraintValidator @NotNull String name,
    @UserNameAndSurnameConstraintValidator @NotNull String surname) {

}
