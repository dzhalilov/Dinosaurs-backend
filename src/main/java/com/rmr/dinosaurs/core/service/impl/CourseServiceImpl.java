package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.CourseAndProfession;
import com.rmr.dinosaurs.core.model.CourseAndTag;
import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.Tag;
import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;
import com.rmr.dinosaurs.core.service.CourseService;
import com.rmr.dinosaurs.infrastucture.database.CourseAndProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.CourseAndTagRepository;
import com.rmr.dinosaurs.infrastucture.database.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.TagRepository;
import java.util.ArrayList;
import java.util.List;
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
  private final TagRepository tagRepo;
  private final CourseAndProfessionRepository capRefRepo;
  private final CourseAndTagRepository catRefRepo;

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

  private void saveNewCapRef(Course course, Profession profession) {
    boolean doesExists = capRefRepo
        .existsByCourse_IdAndProfession_Id(course.getId(), profession.getId());

    if (!doesExists) {
      CourseAndProfession capRef = new CourseAndProfession();
      capRef.setCourse(course);
      capRef.setProfession(profession);
      capRefRepo.save(capRef);
    }
  }

  private void saveNewTagsAndSaveNewCatRefs(Course course, String[] tagValues) {
    boolean isEmptyTags = tagValues == null || tagValues.length == 0;
    if (isEmptyTags) {
      return;
    }

    final int len = tagValues.length;
    List<Tag> savingTags = new ArrayList<>(len);
    List<Tag> existingTags = new ArrayList<>(len);

    for (String v : tagValues) {
      Optional<Tag> optTag = tagRepo.findByValue(v);

      if (optTag.isEmpty()) {
        Tag newTag = new Tag();
        newTag.setValue(v);
        savingTags.add(newTag);
      } else {
        existingTags.add(
            optTag.get()
        );
      }
    }

    List<Tag> savedTags = tagRepo.saveAllAndFlush(savingTags);
    existingTags.addAll(savedTags);

    List<CourseAndTag> catRefs = new ArrayList<>(len);
    for (Tag t : existingTags) {
      CourseAndTag catRef = new CourseAndTag();
      catRef.setCourse(course);
      catRef.setTag(t);
      catRefs.add(catRef);
    }

    catRefRepo.saveAll(catRefs);
  }

}
