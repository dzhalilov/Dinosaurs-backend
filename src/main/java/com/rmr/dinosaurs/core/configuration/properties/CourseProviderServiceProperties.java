package com.rmr.dinosaurs.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services.provider")
@Data
public class CourseProviderServiceProperties {

  private Integer defaultPageSize;

}
