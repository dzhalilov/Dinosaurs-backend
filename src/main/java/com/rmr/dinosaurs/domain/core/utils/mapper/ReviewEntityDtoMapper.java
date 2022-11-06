package com.rmr.dinosaurs.domain.core.utils.mapper;

import com.rmr.dinosaurs.domain.core.model.Review;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewEntityDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "course", ignore = true)
  @Mapping(target = "userInfo", ignore = true)
  Review toEntity(ReviewCreateDto reviewDto);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "rating", source = "rating")
  @Mapping(target = "textReview", source = "textReview")
  @Mapping(target = "userInfoName", source = "userInfo.name")
  @Mapping(target = "userInfoSurname", source = "userInfo.surname")
  @Mapping(target = "created", source = "created")
  ReviewResponseDto toReviewResponseDto(Review review);

}
