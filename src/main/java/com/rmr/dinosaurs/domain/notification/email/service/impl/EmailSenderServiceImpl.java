package com.rmr.dinosaurs.domain.notification.email.service.impl;

import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.FAILED_EMAIL_SEND;
import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.INCORRECT_EMAIL_DATA;

import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.notification.email.service.EmailSenderService;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Profile("emailEnable")
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

  private static final String SENDER_ADDRESS = "itdinosaurs@yandex.ru";

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private final JavaMailSender javaMailSender;


  @Override
  public void sendEmail(MimeMessage message, List<String> recipients) {
    if (Objects.isNull(message) || recipients.isEmpty()) {
      throw new ServiceException(INCORRECT_EMAIL_DATA);
    }
    executorService.submit(() ->
        recipients.forEach(recipient -> {
              try {
                log.info("Sending email to: {}", recipient);
                message.setFrom(SENDER_ADDRESS);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                javaMailSender.send(message);
              } catch (Exception e) {
                log.debug("Failed to send an email", e);
                throw new ServiceException(FAILED_EMAIL_SEND);
              }
            }
        )
    );
  }

  @Override
  public MimeMessage getMimeMessage() {
    return javaMailSender.createMimeMessage();
  }

}
