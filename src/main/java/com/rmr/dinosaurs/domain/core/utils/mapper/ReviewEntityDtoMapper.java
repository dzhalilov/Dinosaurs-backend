package com.rmr.dinosaurs.domain.core.utils.mapper;

import com.rmr.dinosaurs.domain.core.model.Review;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewEntityDtoMapper {

  Review toEntity(ReviewDto reviewDto);

  ReviewDto toReviewDto(Review review);
}
