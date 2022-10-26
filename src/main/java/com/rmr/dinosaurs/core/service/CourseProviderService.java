package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ProviderDto;
import com.rmr.dinosaurs.core.model.dto.ProviderPageDto;
import java.util.List;

public interface CourseProviderService {

  ProviderDto createProvider(ProviderDto provider);

  ProviderDto getProviderById(long id);

  ProviderDto updateProviderById(long id, ProviderDto dto);

  List<ProviderDto> getAllProviders();

  ProviderPageDto getProviderPage(int pageNum);

}
