package com.rmr.dinosaurs.domain.statistics.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services.statistics.course")
@Data
public class CourseStatisticsProperties {

  private Integer defaultPageSize;

}
