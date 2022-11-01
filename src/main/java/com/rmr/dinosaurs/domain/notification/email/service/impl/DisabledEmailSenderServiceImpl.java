package com.rmr.dinosaurs.domain.notification.email.service.impl;

import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.FAILED_EMAIL_SEND;

import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.notification.email.model.EmailMessage;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Profile("emailDisable")
@Slf4j
@Service
public class DisabledEmailSenderServiceImpl extends EmailSenderServiceImpl {

  public DisabledEmailSenderServiceImpl(
      JavaMailSender javaMailSender,
      TempConfirmationService tempConfirmationService) {
    super(javaMailSender, tempConfirmationService);
  }

  @Override
  public void sendEmail(EmailMessage emailMessage) {
    log.info("Sending email");
    log.debug("Message \n{}", emailMessage);

    if (!isEmailMessageValid(emailMessage)) {
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  private boolean isEmailMessageValid(EmailMessage emailMessage) {
    return !Objects.isNull(emailMessage)
        && !CollectionUtils.isEmpty(emailMessage.getRecipients());
  }


}
