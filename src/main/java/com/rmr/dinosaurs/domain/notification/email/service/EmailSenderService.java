package com.rmr.dinosaurs.domain.notification.email.service;

import java.util.List;
import javax.mail.internet.MimeMessage;

public interface EmailSenderService {

  void sendEmail(MimeMessage message, List<String> recipients);

  MimeMessage getMimeMessage();

}
