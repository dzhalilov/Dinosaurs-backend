package com.rmr.dinosaurs.domain.core.service;

import com.rmr.dinosaurs.domain.core.model.dto.ProviderDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProviderPageDto;
import java.util.List;

public interface CourseProviderService {

  /**
   * create course provider profile data
   *
   * @param providerDto to be created
   * @return created course provider profile data
   */
  ProviderDto addProvider(ProviderDto providerDto);

  /**
   * get course provider profile data by provider id
   *
   * @param providerId course provider profile id
   * @return course provider profile
   */
  ProviderDto getProviderById(long providerId);

  /**
   * set course provider profile data
   *
   * @param providerId  course provider profile id to be changed to
   * @param providerDto data to be changed to
   * @return changed course provider profile data
   */
  ProviderDto editProviderById(long providerId, ProviderDto providerDto);

  /**
   * get list of all course provider profiles
   *
   * @return list of course provider profile data
   */
  List<ProviderDto> getAllProviders();

  /**
   * get page of course provider profiles
   *
   * @param pageNum page number
   * @return page of course provider profile data
   */
  ProviderPageDto getProviderPage(int pageNum);

}
