package com.rmr.dinosaurs.domain.auth.service;

import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import com.rmr.dinosaurs.domain.auth.model.User;
import java.util.Optional;
import java.util.UUID;

public interface TempConfirmationService {

  TempConfirmation createTempConfirmationFor(User user);

  Optional<TempConfirmation> validateTempConfirmationByCodeAndDelete(UUID tempCode);

}
