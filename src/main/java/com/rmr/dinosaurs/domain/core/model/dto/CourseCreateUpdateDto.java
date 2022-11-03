package com.rmr.dinosaurs.domain.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateUpdateDto {

    Long id;

    @Length(max = 255)
    String title;

    @Length(max = 255)
    String url;

    @Length(max = 255)
    String coverUrl;

    @Length(max = 4096)
    String description;

    LocalDateTime startsAt;

    LocalDateTime endsAt;

    Boolean isAdvanced;

    Long providerId;

    Long professionId;

    String[] tags;
}
