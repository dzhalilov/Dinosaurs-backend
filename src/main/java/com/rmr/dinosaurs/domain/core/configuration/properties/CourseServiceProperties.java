package com.rmr.dinosaurs.domain.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services.course")
@Data
public class CourseServiceProperties {

  private Integer defaultInternalRating;
  private Boolean defaultIsIndefinite;
  private Boolean defaultIsArchived;

  private Integer defaultPageSize;

}
