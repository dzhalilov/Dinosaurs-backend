package com.rmr.dinosaurs.domain.core.configuration.properties;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services.study")
@Data
public class CourseStudyServiceProperties {

  private Long defaultScore;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime defaultEndsAt;
  private Integer defaultPageSize;
}