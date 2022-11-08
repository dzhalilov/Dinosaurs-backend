package com.rmr.dinosaurs.domain.core.model.dto;

import com.rmr.dinosaurs.domain.auth.utils.validator.EmailConstraintValidator;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public record CourseStudyUpdateDto(
    @EmailConstraintValidator
    String userEmail,
    LocalDateTime endsAt,
    @Min(0)
    @Max(100)
    Long score
) {

}
