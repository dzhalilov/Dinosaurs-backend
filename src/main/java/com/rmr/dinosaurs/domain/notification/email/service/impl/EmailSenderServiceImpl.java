package com.rmr.dinosaurs.domain.notification.email.service.impl;

import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.FAILED_EMAIL_SEND;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.notification.email.model.EmailMessage;
import com.rmr.dinosaurs.domain.notification.email.model.MailType;
import com.rmr.dinosaurs.domain.notification.email.service.EmailSenderService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
  private final TempConfirmationService tempConfirmationService;

  @Value("${notification.email.confirmUrl}")
  private String confirmUrl;


  @Override
  public void sendEmail(EmailMessage emailMessage) {
    MimeMessage message = javaMailSender.createMimeMessage();
    emailMessage.getRecipients().forEach(
        recipient -> executorService.submit(() -> {
          try {
            log.info("Sending email to: {}", recipient.getEmail());
            configureMessageByType(message, emailMessage.getMailType(), recipient);
            javaMailSender.send(message);
          } catch (Exception e) {
            log.debug("Failed to send an email", e);
            throw new ServiceException(FAILED_EMAIL_SEND);
          }
        })
    );
  }

  private void configureMessageByType(MimeMessage message, MailType mailType,
      User recipient) throws ServiceException {
    switch (mailType) {
      case EMAIL_CONFIRM -> configureEmailConfirmation(message, recipient);
      case WELCOME_MAIL -> configureWelcomeEmail(message, recipient);
      default -> {
        log.debug("No email type found");
        throw new ServiceException(FAILED_EMAIL_SEND);
      }
    }
  }

  private void configureWelcomeEmail(MimeMessage message, User recipient) {
    try {
      message.setFrom(new InternetAddress(SENDER_ADDRESS));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.getEmail()));
      message.setSubject("Добро пожаловать в дино-клуб!");
      message.setContent("<h3>Рады приветствовать Вас на сервисе IT Dinosaurs</h3>",
          "text/html;charset=UTF-8");
    } catch (MessagingException e) {
      log.debug("Cant send welcome email", e);
      throw new RuntimeException(e);
    }
  }

  // TODO: could be refactored to another type of message to sent html
  private void configureEmailConfirmation(MimeMessage message, User recipient) {
    var tempConfirmation = tempConfirmationService.createTempConfirmationFor(recipient);
    try {
      message.setFrom(new InternetAddress(SENDER_ADDRESS));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.getEmail()));
      message.setSubject("Подтверждение адреса электронной почты");
      message.setContent(String.format("<h2>Для подтверждения перейдите по ссылке: "
              + "%s/%s </h2>", confirmUrl, tempConfirmation.getId()),
          "text/html;charset=UTF-8"
      );
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

}
