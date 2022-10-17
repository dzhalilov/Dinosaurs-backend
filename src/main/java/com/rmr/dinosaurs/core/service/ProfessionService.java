package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ProfessionDto;

public interface ProfessionService {

  ProfessionDto createProfession(ProfessionDto provider);

  ProfessionDto getProfessionById(long id);

  ProfessionDto updateProfessionById(long id, ProfessionDto dto);

}
