package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.exception.errorcode.CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND;
import static com.rmr.dinosaurs.core.exception.errorcode.PageErrorCode.NEGATIVE_PAGE_NUMBER;

import com.rmr.dinosaurs.core.configuration.properties.CourseProviderServiceProperties;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderDto;
import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderPageDto;
import com.rmr.dinosaurs.core.service.CourseProviderService;
import com.rmr.dinosaurs.core.utils.mapper.CourseProviderEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.CourseProviderRepository;
import java.util.List;
import java.util.function.Supplier;
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

  public static final Supplier<RuntimeException>
      COURSE_PROVIDER_NOT_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(COURSE_PROVIDER_NOT_FOUND);

  private final CourseProviderServiceProperties props;
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
        .orElseThrow(COURSE_PROVIDER_NOT_FOUND_EXCEPTION_SUPPLIER);
    return mapper.toDto(provider);
  }

  @Override
  public CourseProviderDto updateProviderById(long id, CourseProviderDto dto) {
    CourseProvider provider = providerRepo.findById(id)
        .orElseThrow(COURSE_PROVIDER_NOT_FOUND_EXCEPTION_SUPPLIER);

    provider.setName(dto.getName());
    provider.setUrl(dto.getUrl());
    provider.setDescription(dto.getDescription());
    provider.setCoverUrl(dto.getCoverUrl());

    CourseProvider updatedProvider = providerRepo.saveAndFlush(provider);
    return mapper.toDto(updatedProvider);
  }

  @Override
  public List<CourseProviderDto> getAllProviders() {
    return providerRepo.findAll()
        .stream().map(mapper::toDto).toList();
  }

  @Override
  public CourseProviderPageDto getProviderPage(int pageNum) {
    --pageNum;
    if (pageNum < 0) {
      throw new ServiceException(NEGATIVE_PAGE_NUMBER);
    }
    Pageable pageable = PageRequest.of(
        pageNum, props.getDefaultPageSize());

    Page<CourseProvider> page = providerRepo
        .findByOrderByNameAsc(pageable);

    CourseProviderPageDto pageDto = new CourseProviderPageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(mapper::toDto).toList());

    return pageDto;
  }

}
