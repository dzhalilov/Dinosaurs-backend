package com.rmr.dinosaurs.domain.core.service;

import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionPageDto;
import java.util.List;

public interface ProfessionService {

  /**
   * create profession profile data
   *
   * @param professionDto to be created
   * @return created profession profile data
   */
  ProfessionDto addProfession(ProfessionDto professionDto);

  /**
   * get profession profile data by profession id
   *
   * @param professionId profession profile id
   * @return profession profile
   */
  ProfessionDto getProfessionById(long professionId);

  /**
   * set profession profile data
   *
   * @param professionId  profession profile id to be changed to
   * @param professionDto to be changed to
   * @return changed course profile data
   */
  ProfessionDto editProfessionById(long professionId, ProfessionDto professionDto);

  /**
   * get list of all profession profiles
   *
   * @return list of profession profile data
   */
  List<ProfessionDto> getAllProfessions();

  /**
   * get page of profession profiles
   *
   * @param pageNum page number
   * @return page of profession profile data
   */
  ProfessionPageDto getProfessionPage(int pageNum);

}
