package com.rmr.dinosaurs.domain.core.utils.mapper;

import com.rmr.dinosaurs.domain.core.model.Review;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewEntityDtoMapper {

  Review toEntity(ReviewCreateDto reviewDto);

  @Mapping(target = "userInfoName", source = "userInfo.name")
  @Mapping(target = "userInfoSurname", source = "userInfo.surname")
  ReviewResponseDto toReviewResponseDto(Review review);

  List<ReviewResponseDto> toReviewResponseDtoList(List<Review> reviewList);
}
