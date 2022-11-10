package com.rmr.dinosaurs.domain.auth.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.TempConfirmationErrorCode.CONFIRMATION_CODE_EXPIRED;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.TempConfirmationErrorCode.CONFIRMATION_CODE_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.infrastucture.database.auth.TempConfirmationRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TempConfirmationServiceImpl implements TempConfirmationService {

  private final TempConfirmationRepository tempConfirmationRepository;
  @Value("${tempconfirmation.code.ttl}")
  public Long tempCodeTtl;
  private final BiPredicate<LocalDateTime, LocalDateTime> isValidTempConfirmationPredicate =
      (currentTime, issuedAt) -> currentTime.isBefore(
          issuedAt.plus(tempCodeTtl, ChronoUnit.MINUTES));


  @Override
  public TempConfirmation createTempConfirmationFor(User user) {
    var tmpConfirmation = new TempConfirmation(null, LocalDateTime.now(ZoneOffset.UTC), user);
    return tempConfirmationRepository.saveAndFlush(tmpConfirmation);
  }

  @Override
  public Optional<TempConfirmation> validateTempConfirmationByCodeAndDelete(UUID tempCode) {
    var tempConfirmation = tempConfirmationRepository.findById(tempCode)
        .orElseThrow(() -> {
          log.info("Temp confirmation was not found");
          throw new ServiceException(CONFIRMATION_CODE_NOT_FOUND);
        });
    if (!isValidTempConfirmationPredicate.test(LocalDateTime.now(ZoneOffset.UTC),
        tempConfirmation.getIssuedAt())) {
      throw new ServiceException(CONFIRMATION_CODE_EXPIRED);
    }
    tempConfirmationRepository.delete(tempConfirmation);
    return Optional.of(tempConfirmation);
  }

  @Scheduled(cron = "@midnight")
  void removeNonConfirmed() {
    tempConfirmationRepository.deleteAllByIssuedAtBefore(
        LocalDateTime.now(ZoneOffset.UTC).minus(tempCodeTtl, ChronoUnit.MINUTES));
  }

}
