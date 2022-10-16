package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;
import com.rmr.dinosaurs.core.service.CourseService;
import com.rmr.dinosaurs.infrastucture.database.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseProviderRepository providerRepo;
  private final ProfessionRepository professionRepo;

  @Override
  public CourseDto addCourse(CreatingCourseDto course) {
    return null;
  }

  private CourseProvider findCourseProviderOrSaveNewAndFlush(String providerUrl) {
    Optional<CourseProvider> optFoundProvider = providerRepo
        .findByUrl(providerUrl);

    CourseProvider provider;
    if (optFoundProvider.isEmpty()) {
      provider = new CourseProvider();
      provider.setUrl(providerUrl);
      provider = providerRepo.saveAndFlush(provider);
    } else {
      provider = optFoundProvider.get();
    }

    return provider;
  }

  private Profession findProfessionOrSaveNewAndFlush(String professionName) {
    Optional<Profession> optFoundProfession = professionRepo
        .findByName(professionName);

    Profession profession;
    if (optFoundProfession.isEmpty()) {
      profession = new Profession();
      profession.setName(professionName);
      profession = professionRepo.saveAndFlush(profession);
    } else {
      profession = optFoundProfession.get();
    }

    return profession;
  }

}
