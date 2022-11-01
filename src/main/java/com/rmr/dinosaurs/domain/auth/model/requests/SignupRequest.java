package com.rmr.dinosaurs.domain.auth.model.requests;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import com.rmr.dinosaurs.domain.auth.utils.validator.PasswordConstraintValidator;
import javax.validation.constraints.Size;

public record SignupRequest(
    @EmailConstraintValidator String email,
    @PasswordConstraintValidator String password,
    @Size(min = 1, max = 100) String name,
    @Size(min = 1, max = 100) String surname) {

}
