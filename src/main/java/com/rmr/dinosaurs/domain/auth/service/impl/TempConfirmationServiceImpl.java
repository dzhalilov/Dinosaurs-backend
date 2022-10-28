package com.rmr.dinosaurs.domain.auth.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.TempConfirmationErrorCode.CONFIRMATION_CODE_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.infrastucture.database.auth.TempConfirmationRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TempConfirmationServiceImpl implements TempConfirmationService {

  private static final Long TTL = 60L;

  private final TempConfirmationRepository tempConfirmationRepository;


  @Override
  public TempConfirmation createTempConfirmationFor(User user) {
    var tmpConfirmation = new TempConfirmation(null, LocalDateTime.now(), user);
    return tempConfirmationRepository.saveAndFlush(tmpConfirmation);
  }

  @Override
  public Optional<TempConfirmation> validateTempConfirmationByCodeAndDelete(UUID tempCode) {
    var tempConfirmation = tempConfirmationRepository.findById(tempCode)
        .orElseThrow(() -> {
          log.info("Temp confirmation was not found");
          throw new ServiceException(CONFIRMATION_CODE_NOT_FOUND);
        });
    tempConfirmationRepository.delete(tempConfirmation);
    return Optional.of(tempConfirmation);
  }

  @Scheduled(cron = "@midnight")
  private void removeNonConfirmed() {
    // TODO: could be optimised to use just a query
    var overDueTempConfirmations = tempConfirmationRepository.findAll()
        .stream()
        .filter(tmpConf -> Instant.now().minus(TTL, ChronoUnit.MINUTES).isBefore(
            Instant.from(tmpConf.getIssuedAt())))
        .toList();
    tempConfirmationRepository.deleteAll(overDueTempConfirmations);
  }

}
