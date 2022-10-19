package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.configuration.properties.CourseServiceProperties;
import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.CourseAndProfession;
import com.rmr.dinosaurs.core.model.CourseAndTag;
import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.Tag;
import com.rmr.dinosaurs.core.model.dto.CreateUpdateCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCoursePageDto;
import com.rmr.dinosaurs.core.service.CourseService;
import com.rmr.dinosaurs.core.service.exceptions.CourseNotFoundException;
import com.rmr.dinosaurs.core.service.exceptions.CourseProviderNotFoundException;
import com.rmr.dinosaurs.core.service.exceptions.NegativePageNumberException;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.CourseEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.CourseAndProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.CourseAndTagRepository;
import com.rmr.dinosaurs.infrastucture.database.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseServiceProperties props;
  private final CourseEntityDtoMapper mapper;

  private final CourseRepository courseRepo;
  private final CourseProviderRepository providerRepo;
  private final ProfessionRepository professionRepo;
  private final TagRepository tagRepo;
  private final CourseAndProfessionRepository capRefRepo;
  private final CourseAndTagRepository catRefRepo;

  @Override
  @Transactional
  public CreateUpdateCourseDto createCourse(CreateUpdateCourseDto dto) {
    CourseProvider provider = providerRepo.findById(dto.getProviderId())
        .orElseThrow(CourseProviderNotFoundException::new);
    Course course = saveNewCourseAndFlush(mapper.toEntity(dto), provider);

    Profession profession = professionRepo.findById(dto.getProfessionId())
        .orElseThrow(ProfessionNotFoundException::new);
    saveNewCapRef(course, profession);

    saveNewTagsAndSaveNewCatRefs(course, dto.getTags());

    CreateUpdateCourseDto createdCourse = mapper.toDto(course);
    createdCourse.setProfessionId(dto.getProfessionId());
    createdCourse.setTags(dto.getTags());
    return createdCourse;
  }

  @Override
  @Transactional
  public ReadCourseDto getCourseById(long id) {
    Course course = courseRepo.findById(id)
        .orElseThrow(CourseNotFoundException::new);
    return toReadCourseDto(course);
  }

  @Override
  @Transactional
  public CreateUpdateCourseDto updateCourseById(long id, CreateUpdateCourseDto dto) {
    CourseProvider provider = providerRepo.findById(dto.getProviderId())
        .orElseThrow(CourseProviderNotFoundException::new);
    Course course = courseRepo.findById(id)
        .orElseThrow(CourseNotFoundException::new);
    course.setProvider(provider);
    course.setTitle(dto.getTitle());
    course.setUrl(dto.getUrl());
    course.setCoverUrl(dto.getCoverUrl());
    course.setDescription(dto.getDescription());
    course.setStartsAt(dto.getStartsAt());
    course.setEndsAt(dto.getEndsAt());
    course.setIsAdvanced(dto.getIsAdvanced());
    Course updatedCourse = courseRepo.saveAndFlush(course);

    Profession profession = professionRepo.findById(dto.getProfessionId())
        .orElseThrow(ProfessionNotFoundException::new);
    capRefRepo.deleteAllByCourse_Id(updatedCourse.getId());
    saveNewCapRef(updatedCourse, profession);

    catRefRepo.deleteAllByCourse_Id(updatedCourse.getId());
    saveNewTagsAndSaveNewCatRefs(updatedCourse, dto.getTags());

    CreateUpdateCourseDto updatedDto = mapper.toDto(updatedCourse);
    updatedDto.setProfessionId(dto.getProfessionId());
    updatedDto.setTags(dto.getTags());
    return updatedDto;
  }

  @Override
  @Transactional
  public List<ReadCourseDto> getAllCourses() {
    return courseRepo.findAll()
        .stream().map(this::toReadCourseDto).toList();
  }

  @Override
  @Transactional
  public ReadCoursePageDto getCoursePage(int pageNum) {
    --pageNum;
    if (pageNum < 0) {
      throw new NegativePageNumberException();
    }
    Pageable pageable = PageRequest.of(
        pageNum, props.getDefaultPageSize());

    Page<Course> page = courseRepo
        .findByIsArchivedFalseOrderByStartsAtAsc(pageable);

    ReadCoursePageDto pageDto = new ReadCoursePageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(this::toReadCourseDto).toList());

    return pageDto;
  }

  private Course saveNewCourseAndFlush(Course course, CourseProvider provider) {
    course.setProvider(provider);

    course.setInternalRating(props.getDefaultInternalRating());
    course.setIsIndefinite(props.getDefaultIsIndefinite());
    course.setIsArchived(props.getDefaultIsArchived());

    return courseRepo.saveAndFlush(course);
  }

  private void saveNewCapRef(Course course, Profession profession) {
    boolean doesExist = capRefRepo
        .existsByCourse_IdAndProfession_Id(course.getId(), profession.getId());
    if (doesExist) {
      return;
    }

    CourseAndProfession newCapRef = new CourseAndProfession();
    newCapRef.setCourse(course);
    newCapRef.setProfession(profession);
    capRefRepo.save(newCapRef);
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

  private ReadCourseDto toReadCourseDto(Course course) {
    Profession courseProfession = course.getCourseAndProfessionRefs()
        .iterator().next()
        .getProfession();

    List<String> tags = course.getCourseAndTagRefs().stream()
        .map(cat -> cat.getTag().getValue())
        .toList();

    ReadCourseDto readCourseDto = mapper.toReadCourseDto(course);
    readCourseDto.setProfessionId(courseProfession.getId());
    readCourseDto.setProfessionName(courseProfession.getName());
    readCourseDto.setTags(tags);

    return readCourseDto;
  }

}
