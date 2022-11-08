package com.rmr.dinosaurs.domain.notification.email.service.impl;

import static com.rmr.dinosaurs.domain.notification.email.exception.errorcode.MailErrorCode.FAILED_EMAIL_SEND;

import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.notification.email.service.EmailSenderService;
import com.rmr.dinosaurs.domain.notification.email.service.EmailService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

  private final EmailSenderService emailSenderService;

  @Value("${notification.email.domainUrl}")
  private String domainUrl;


  @Override
  public void sendEmailConfirmationEmail(UUID tempConfirmationId, String recipient) {
    MimeMessage message = emailSenderService.getMimeMessage();
    try {
      message.setSubject("Подтверждение адреса электронной почты");
      message.setContent(String.format("<h2>Для подтверждения перейдите по ссылке: "
              + "%s/%s </h2>", domainUrl + "/confirm", tempConfirmationId),
          CONTENT_TYPE
      );
      emailSenderService.sendEmail(message, Collections.singletonList(recipient));
    } catch (MessagingException e) {
      log.debug("Cant send confirmation email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendWelcomeRegistrationEmail(String recipient) {
    MimeMessage message = emailSenderService.getMimeMessage();
    try {
      message.setSubject("Добро пожаловать в дино-клуб!");
      message.setContent("<h3>Рады приветствовать Вас на сервисе IT Dinosaurs</h3>",
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, Collections.singletonList(recipient));
    } catch (MessagingException e) {
      log.debug("Cant send welcome email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendChangeRoleEmail(@NotNull Boolean isModerator, String recipient) {
    MimeMessage message = emailSenderService.getMimeMessage();
    try {
      message.setSubject("Уведомление: изменение роли");
      String role = Boolean.TRUE.equals(isModerator) ? "Модератор" : "Пользователь";
      message.setContent("<h4>Ваша роль в IT Dinosaurs изменена.</br>"
              + "Текущая роль: " + role + "</h4>",
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, Collections.singletonList(recipient));
    } catch (MessagingException e) {
      log.debug("Cant send welcome email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendInvalidCoursesLinksEmail(List<Course> courses, List<String> recipients) {
    MimeMessage message = emailSenderService.getMimeMessage();
    try {
      message.setSubject("Уведомление: Ссылки на курсы недоступны");
      message.setContent("<h3>Список курсов, ссылки на которые недоступны</h3></br>"
              + getHtmlListOfCourses(courses),
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, recipients);
    } catch (MessagingException e) {
      log.debug("Cant send ended today courses email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendEndedTodayCoursesEmail(List<Course> courses, List<String> recipients) {
    MimeMessage message = emailSenderService.getMimeMessage();
    try {
      message.setSubject("Уведомление: Курсы заканчиваются сегодня");
      message.setContent("<h3>Список курсов, которые закончились сегодня</h3></br>"
              + getHtmlListOfCourses(courses),
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, recipients);
    } catch (MessagingException e) {
      log.debug("Cant send ended today courses email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  private String getHtmlListOfCourses(List<Course> courses) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<ul>");
    courses.forEach(course ->
        stringBuilder.append("<li> <a href='")
            .append(domainUrl).append("/admin/courses/").append(course.getId()).append("/edit")
            .append("'>")
            .append(course.getTitle())
            .append("</a>")
            .append("</li>")
    );
    stringBuilder.append("</ul>");
    return stringBuilder.toString();
  }

}
