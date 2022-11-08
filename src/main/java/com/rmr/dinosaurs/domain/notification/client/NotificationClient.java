package com.rmr.dinosaurs.domain.notification.client;

import com.rmr.dinosaurs.domain.core.model.Course;
import java.util.List;
import java.util.UUID;

public interface NotificationClient {

  void emailConfirmationNotification(UUID tempConfirmation, String recipient);

  void registrationWelcomeNotification(String recipient);

  void roleChangedNotification(Boolean changedTo, String recipient);

  void invalidCoursesLinksNotification(List<Course> courses, List<String> recipients);

  void endedTodayCoursesNotification(List<Course> courses, List<String> recipients);

}
