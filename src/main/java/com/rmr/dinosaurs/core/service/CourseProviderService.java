package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CourseProviderDto;

public interface CourseProviderService {

  CourseProviderDto createProvider(CourseProviderDto provider);

  CourseProviderDto getProviderById(long id);

  CourseProviderDto updateProviderById(long id, CourseProviderDto dto);

}
