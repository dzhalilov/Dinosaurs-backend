package com.rmr.dinosaurs.domain.core.service.impl;

import static com.rmr.dinosaurs.domain.core.exception.errorcode.PageErrorCode.NEGATIVE_PAGE_NUMBER;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ProfessionErrorCode.PROFESSION_ALREADY_EXISTS;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;

import com.rmr.dinosaurs.domain.core.configuration.properties.ProfessionServiceProperties;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionPageDto;
import com.rmr.dinosaurs.domain.core.service.ProfessionService;
import com.rmr.dinosaurs.domain.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
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

  private final ProfessionRepository repo;

  @Override
  public ProfessionDto addProfession(ProfessionDto dto) {
    if (repo.findByName(dto.getName()).isPresent()) {
      throw new ServiceException(PROFESSION_ALREADY_EXISTS);
    }
    Profession newProfession = mapper.toEntity(dto);
    Profession savedProfession = repo.saveAndFlush(newProfession);
    return mapper.toDto(savedProfession);
  }

  @Override
  public ProfessionDto getProfessionById(long id) {
    Profession profession = repo.findById(id)
        .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));
    return mapper.toDto(profession);
  }

  @Override
  public ProfessionDto editProfessionById(long id, ProfessionDto dto) {
    Profession profession = repo.findById(id)
        .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));

    profession.setName(dto.getName());
    profession.setCoverUrl(dto.getCoverUrl());
    profession.setDescription(dto.getDescription());

    Profession updatedProfession = repo.saveAndFlush(profession);
    return mapper.toDto(updatedProfession);
  }

  @Override
  public List<ProfessionDto> getAllProfessions() {
    return repo.findAll()
        .stream().map(mapper::toDto).toList();
  }

  @Override
  public ProfessionPageDto getProfessionPage(int pageNum) {
    --pageNum;
    if (pageNum < 0) {
      throw new ServiceException(NEGATIVE_PAGE_NUMBER);
    }
    Pageable pageable = PageRequest.of(
        pageNum, props.getDefaultPageSize());

    Page<Profession> page = repo
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
