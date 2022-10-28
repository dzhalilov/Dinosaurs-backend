package com.rmr.dinosaurs.domain.auth.model.requests;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import com.rmr.dinosaurs.domain.auth.utils.validator.PasswordConstraintValidator;

public record LoginRequest(
    @EmailConstraintValidator String email,
    @PasswordConstraintValidator String password) {

}
