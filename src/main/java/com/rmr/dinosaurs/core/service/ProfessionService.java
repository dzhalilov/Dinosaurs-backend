package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.ProfessionPageDto;
import java.util.List;

public interface ProfessionService {

  ProfessionDto addProfession(ProfessionDto provider);

  ProfessionDto getProfessionById(long id);

  ProfessionDto editProfessionById(long id, ProfessionDto dto);

  List<ProfessionDto> getAllProfessions();

  ProfessionPageDto getProfessionPage(int pageNum);

}
