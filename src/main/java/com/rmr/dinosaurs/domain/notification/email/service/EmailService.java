package com.rmr.dinosaurs.domain.notification.email.service;

import com.rmr.dinosaurs.domain.core.model.Course;
import java.util.List;
import java.util.UUID;

public interface EmailService {

  /**
   * send generated link to confirm email
   *
   * @param tempConfirmationId temp confirmation id
   * @param recipient          email of recipient
   */
  void sendEmailConfirmationEmail(UUID tempConfirmationId, String recipient);

  /**
   * send welcome registration email
   *
   * @param recipient email of recipient
   */
  void sendWelcomeRegistrationEmail(String recipient);

  /**
   * send email notification about role changed
   *
   * @param isModerator new value of isModerator
   * @param recipient   email of recipient
   */
  void sendChangeRoleEmail(Boolean isModerator, String recipient);

  /**
   * send email notification about incorrect course link
   *
   * @param courses    courses with incorrect link
   * @param recipients emails of recipients
   */
  void sendInvalidCoursesLinksEmail(List<Course> courses, List<String> recipients);

  /**
   * send email notification about ended today courses
   *
   * @param courses    courses ended today
   * @param recipients emails of recipients
   */
  void sendEndedTodayCoursesEmail(List<Course> courses, List<String> recipients);

}
