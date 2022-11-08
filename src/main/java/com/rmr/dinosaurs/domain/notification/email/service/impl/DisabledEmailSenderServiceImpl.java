package com.rmr.dinosaurs.domain.notification.email.service.impl;

import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.FAILED_EMAIL_SEND;

import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import java.util.List;
import java.util.Objects;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Profile("emailDisable")
@Slf4j
@Service
public class DisabledEmailSenderServiceImpl extends EmailSenderServiceImpl {

  public DisabledEmailSenderServiceImpl(JavaMailSender javaMailSender) {
    super(javaMailSender);
  }

  @Override
  public void sendEmail(MimeMessage message, List<String> recipients) {
    log.info("Sending email to {}", recipients.toString());

    if (Objects.isNull(message) && recipients.isEmpty()) {
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

}
