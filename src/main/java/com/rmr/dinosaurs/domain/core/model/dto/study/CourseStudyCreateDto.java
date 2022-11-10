package com.rmr.dinosaurs.domain.core.model.dto.study;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public record CourseStudyCreateDto(
    @NotNull
    LocalDateTime startsAt
) {

}
