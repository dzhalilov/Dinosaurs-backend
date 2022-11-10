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
  private static final String COFFEE_SMILE = "&#9749;";
  private static final String WEB_SITE_NAME = "Динозавры в IT";

  private final EmailSenderService emailSenderService;

  @Value("${notification.email.domainUrl}")
  private String domainUrl;


  @Override
  public void sendEmailConfirmationEmail(UUID tempConfirmationId, String recipient) {
    try {
      MimeMessage message = emailSenderService.getMimeMessage();
      message.setSubject("Подтверждение адреса электронной почты");
      message.setContent(String.format("<h2>Для подтверждения перейдите по ссылке: %s/%s </h2>"
                  + " <br><br>Если вы не регистрировались на сайте %s, то игнорируйте это письмо.",
              domainUrl + "/confirm", tempConfirmationId, domainUrl),
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
    try {
      MimeMessage message = emailSenderService.getMimeMessage();
      message.setSubject("Добро пожаловать");
      message.setContent(String.format(
              "<h2>Рады приветствовать Вас в нашем уютном %s дино-клубе!</h2>"
                  + "<br><h2>Будем всегда рады видеть Вас на %s </h2>", COFFEE_SMILE, domainUrl),
          CONTENT_TYPE
      );
      emailSenderService.sendEmail(message, Collections.singletonList(recipient));
    } catch (MessagingException e) {
      log.debug("Cant send welcome email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendChangeRoleEmail(@NotNull Boolean isModerator, String recipient) {
    try {
      MimeMessage message = emailSenderService.getMimeMessage();
      message.setSubject("Уведомление: изменение роли");
      String role = Boolean.TRUE.equals(isModerator) ? "Модератор" : "Пользователь";
      message.setContent(String.format("<h4>Ваша роль на сайте %s изменена.</h4>"
                  + "<br><h4>Текущая роль: %s </h4><br><br>%s %s",
              WEB_SITE_NAME, role, getChangeRoleMessageByRole(isModerator), domainUrl),
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, Collections.singletonList(recipient));
    } catch (MessagingException e) {
      log.debug("Cant send welcome email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  @Override
  public void sendInvalidCoursesLinksEmail(List<Course> courses, List<String> recipients) {
    try {
      MimeMessage message = emailSenderService.getMimeMessage();
      message.setSubject("Уведомление: ссылки на курсы недоступны");
      message.setContent("<h3>Уважаемый модератор! Обратите внимание на список курсов, "
              + " ссылки на которые недоступны: </h3><br>"
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
    try {
      MimeMessage message = emailSenderService.getMimeMessage();
      message.setSubject("Уведомление: сегодня закончились курсы");
      message.setContent("<h3>Уважаемый модератор! Обратите внимание на список курсов, "
              + "которые закончились сегодня: </h3><br>"
              + getHtmlListOfCourses(courses),
          CONTENT_TYPE);
      emailSenderService.sendEmail(message, recipients);
    } catch (MessagingException e) {
      log.debug("Cant send ended today courses email", e);
      throw new ServiceException(FAILED_EMAIL_SEND);
    }
  }

  private String getChangeRoleMessageByRole(Boolean isModer) {
    return Boolean.TRUE.equals(isModer)
        ? "Теперь вам доступна административная панель для добавления и изменения профессий, "
        + "провайдеров курсов и курсов на "
        : "Вам более недоступна административная панель на ";
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
