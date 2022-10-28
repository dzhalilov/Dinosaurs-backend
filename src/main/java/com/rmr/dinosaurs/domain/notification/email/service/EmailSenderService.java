package com.rmr.dinosaurs.domain.notification.email.service;

import com.rmr.dinosaurs.domain.notification.email.model.EmailMessage;

public interface EmailSenderService {

  void sendEmail(EmailMessage emailMessage);

}
