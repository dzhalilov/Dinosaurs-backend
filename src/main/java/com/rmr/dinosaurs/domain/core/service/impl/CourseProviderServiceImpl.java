package com.rmr.dinosaurs.domain.core.service.impl;

import com.rmr.dinosaurs.domain.core.configuration.properties.CourseProviderServiceProperties;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.exception.errorcode.CourseProviderErrorCode;
import com.rmr.dinosaurs.domain.core.exception.errorcode.PageErrorCode;
import com.rmr.dinosaurs.domain.core.model.CourseProvider;
import com.rmr.dinosaurs.domain.core.model.dto.ProviderDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProviderPageDto;
import com.rmr.dinosaurs.domain.core.service.CourseProviderService;
import com.rmr.dinosaurs.domain.core.utils.mapper.CourseProviderEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.core.CourseProviderRepository;
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
public class CourseProviderServiceImpl implements CourseProviderService {

  private final CourseProviderServiceProperties props;
  private final CourseProviderEntityDtoMapper mapper;

  private final CourseProviderRepository providerRepo;

  @Override
  public ProviderDto addProvider(ProviderDto dto) {
    CourseProvider newProvider = mapper.toEntity(dto);
    CourseProvider savedProvider = providerRepo.saveAndFlush(newProvider);
    return mapper.toDto(savedProvider);
  }

  @Override
  public ProviderDto getProviderById(long id) {
    CourseProvider provider = providerRepo.findById(id)
        .orElseThrow(() -> new ServiceException(CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND));
    return mapper.toDto(provider);
  }

  @Override
  public ProviderDto editProviderById(long id, ProviderDto dto) {
    CourseProvider provider = providerRepo.findById(id)
        .orElseThrow(() -> new ServiceException(CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND));

    provider.setName(dto.getName());
    provider.setUrl(dto.getUrl());
    provider.setDescription(dto.getDescription());
    provider.setCoverUrl(dto.getCoverUrl());

    CourseProvider updatedProvider = providerRepo.saveAndFlush(provider);
    return mapper.toDto(updatedProvider);
  }

  @Override
  public List<ProviderDto> getAllProviders() {
    return providerRepo.findAll()
        .stream().map(mapper::toDto).toList();
  }

  @Override
  public ProviderPageDto getProviderPage(int pageNum) {
    --pageNum;
    if (pageNum < 0) {
      throw new ServiceException(PageErrorCode.NEGATIVE_PAGE_NUMBER);
    }
    Pageable pageable = PageRequest.of(
        pageNum, props.getDefaultPageSize());

    Page<CourseProvider> page = providerRepo
        .findByOrderByNameAsc(pageable);

    ProviderPageDto pageDto = new ProviderPageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(mapper::toDto).toList());

    return pageDto;
  }

}
