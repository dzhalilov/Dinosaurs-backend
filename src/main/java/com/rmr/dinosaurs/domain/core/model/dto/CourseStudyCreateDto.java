package com.rmr.dinosaurs.domain.core.model.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public record CourseStudyCreateDto(
    @NotNull
    LocalDateTime startsAt
) {

}
