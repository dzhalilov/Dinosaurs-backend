package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.profession.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.profession.ProfessionPageDto;
import java.util.List;

public interface ProfessionService {

  ProfessionDto createProfession(ProfessionDto provider);

  ProfessionDto getProfessionById(long id);

  ProfessionDto updateProfessionById(long id, ProfessionDto dto);

  List<ProfessionDto> getAllProfessions();

  ProfessionPageDto getProfessionPage(int pageNum);

}
