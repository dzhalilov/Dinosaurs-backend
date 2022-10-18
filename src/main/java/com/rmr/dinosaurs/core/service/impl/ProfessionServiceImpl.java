package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.configuration.properties.ProfessionServiceProperties;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.ProfessionPageDto;
import com.rmr.dinosaurs.core.service.ProfessionService;
import com.rmr.dinosaurs.core.service.exceptions.NegativePageNumberException;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionServiceImpl implements ProfessionService {

  private final ProfessionServiceProperties props;
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

  @Override
  public List<ProfessionDto> getAllProfessions() {
    List<Profession> professions = professionRepo.findAll();

    List<ProfessionDto> dtoList = new ArrayList<>(professions.size());
    for (Profession p : professions) {
      dtoList.add(mapper.toDto(p));
    }

    return dtoList;
  }

  @Override
  public ProfessionPageDto getProfessionPage(int pageNum) {
    --pageNum;
    if (pageNum < 0) {
      throw new NegativePageNumberException();
    }
    Pageable pageable = PageRequest.of(
        pageNum, props.getDefaultPageSize());

    Page<Profession> page = professionRepo
        .findByOrderByNameAsc(pageable);

    ProfessionPageDto pageDto = new ProfessionPageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(mapper::toDto).toList());

    return pageDto;
  }

}
