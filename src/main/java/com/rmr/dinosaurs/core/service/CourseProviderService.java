package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderDto;
import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderPageDto;
import java.util.List;

public interface CourseProviderService {

  CourseProviderDto createProvider(CourseProviderDto provider);

  CourseProviderDto getProviderById(long id);

  CourseProviderDto updateProviderById(long id, CourseProviderDto dto);

  List<CourseProviderDto> getAllProviders();

  CourseProviderPageDto getProviderPage(int pageNum);

}
