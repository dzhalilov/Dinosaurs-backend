package com.rmr.dinosaurs.core.model;

import com.rmr.dinosaurs.core.utils.validation.EmailConstraintValidator;
import com.rmr.dinosaurs.core.utils.validation.PasswordConstraintValidator;

public record LoginRequest(
    @EmailConstraintValidator String email,
    @PasswordConstraintValidator String password) {

}
