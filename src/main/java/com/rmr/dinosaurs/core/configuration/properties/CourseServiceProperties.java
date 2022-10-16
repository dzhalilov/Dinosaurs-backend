package com.rmr.dinosaurs.core.configuration.properties;

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

}
