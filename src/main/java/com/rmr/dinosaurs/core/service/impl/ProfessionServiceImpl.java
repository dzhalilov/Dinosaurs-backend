package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.service.ProfessionService;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionServiceImpl implements ProfessionService {

  private final ProfessionEntityDtoMapper mapper;

  private final ProfessionRepository professionRepo;

  @Override
  public ProfessionDto createProfession(ProfessionDto dto) {
    Profession newProfession = mapper.toEntity(dto);
    Profession savedProvider = professionRepo.saveAndFlush(newProfession);
    return mapper.toDto(savedProvider);
  }

  @Override
  public ProfessionDto getProfessionById(long id) {
    Profession profession = professionRepo.findById(id)
        .orElseThrow(ProfessionNotFoundException::new);
    return mapper.toDto(profession);
  }

  @Override
  public ProfessionDto updateProfessionById(long id, ProfessionDto dto) {
    Profession profession = professionRepo.findById(id)
        .orElseThrow(ProfessionNotFoundException::new);

    profession.setName(dto.getName());
    profession.setDescription(dto.getDescription());
    profession.setCoverUrl(dto.getCoverUrl());

    Profession updatedProfession = professionRepo.saveAndFlush(profession);
    return mapper.toDto(updatedProfession);
  }

}
