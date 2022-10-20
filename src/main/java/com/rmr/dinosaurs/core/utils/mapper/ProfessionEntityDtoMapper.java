package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.profession.ProfessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessionEntityDtoMapper {

  @Mapping(target = "shortDescription", ignore = true)
  @Mapping(target = "userInfos", ignore = true)
  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "courseAndProfessionRefs", ignore = true)
  Profession toEntity(ProfessionDto dto);

  ProfessionDto toDto(Profession entity);

}
