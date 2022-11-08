package com.rmr.dinosaurs.domain.core.model.dto;

import java.time.LocalDateTime;

public record CourseStudyUpdateDto(
    LocalDateTime endsAt,
    Long score
) {

}
