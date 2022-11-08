package com.rmr.dinosaurs.domain.notification.client.impl;

import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.notification.client.NotificationClient;
import com.rmr.dinosaurs.domain.notification.email.service.EmailService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationClientImpl implements NotificationClient {

  private final EmailService emailService;

  @Override
  public void emailConfirmationNotification(UUID tempConfirmation, String recipient) {
    emailService.sendEmailConfirmationEmail(tempConfirmation, recipient);
  }

  @Override
  public void registrationWelcomeNotification(String recipient) {
    emailService.sendWelcomeRegistrationEmail(recipient);
  }

  @Override
  public void roleChangedNotification(Boolean changedTo, String recipient) {
    emailService.sendChangeRoleEmail(changedTo, recipient);
  }

  @Override
  public void invalidCoursesLinksNotification(List<Course> courses, List<String> recipients) {
    emailService.sendInvalidCoursesLinksEmail(courses, recipients);
  }

  @Override
  public void endedTodayCoursesNotification(List<Course> courses, List<String> recipients) {
    emailService.sendEndedTodayCoursesEmail(courses, recipients);
  }

}
