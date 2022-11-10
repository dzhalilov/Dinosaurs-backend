package com.rmr.dinosaurs.domain.core.model.dto.course_study;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public record CourseStudyCreateDto(
    @NotNull
    LocalDateTime startsAt
) {

}
