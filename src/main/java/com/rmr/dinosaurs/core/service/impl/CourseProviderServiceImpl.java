package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.dto.CourseProviderDto;
import com.rmr.dinosaurs.core.service.CourseProviderService;
import com.rmr.dinosaurs.core.service.exceptions.CourseProviderNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.CourseProviderEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.CourseProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseProviderServiceImpl implements CourseProviderService {

  private final CourseProviderEntityDtoMapper mapper;

  private final CourseProviderRepository providerRepo;

  @Override
  public CourseProviderDto createProvider(CourseProviderDto dto) {
    CourseProvider newProvider = mapper.toEntity(dto);
    CourseProvider savedProvider = providerRepo.saveAndFlush(newProvider);
    return mapper.toDto(savedProvider);
  }

  @Override
  public CourseProviderDto getProviderById(long id) {
    CourseProvider provider = providerRepo.findById(id)
        .orElseThrow(CourseProviderNotFoundException::new);
    return mapper.toDto(provider);
  }

  @Override
  public CourseProviderDto updateProviderById(long id, CourseProviderDto dto) {
    CourseProvider provider = providerRepo.findById(id)
        .orElseThrow(CourseProviderNotFoundException::new);

    provider.setName(dto.getName());
    provider.setUrl(dto.getUrl());
    provider.setDescription(dto.getDescription());
    provider.setCoverUrl(dto.getCoverUrl());

    CourseProvider updatedProvider = providerRepo.saveAndFlush(provider);
    return mapper.toDto(updatedProvider);
  }

}
