package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.exception.errorcode.CourseErrorCode.COURSE_NOT_FOUND;
import static com.rmr.dinosaurs.core.exception.errorcode.CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND;
import static com.rmr.dinosaurs.core.exception.errorcode.PageErrorCode.NEGATIVE_PAGE_NUMBER;
import static com.rmr.dinosaurs.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;

import com.rmr.dinosaurs.core.configuration.properties.CourseServiceProperties;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.CourseAndProfession;
import com.rmr.dinosaurs.core.model.CourseAndTag;
import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.Tag;
import com.rmr.dinosaurs.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.core.model.dto.FilterParamsDto;
import com.rmr.dinosaurs.core.service.CourseService;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseServiceProperties props;
  private final CourseEntityDtoMapper courseMapper;

  private final CourseRepository courseRepo;
  private final CourseProviderRepository providerRepo;
  private final ProfessionRepository professionRepo;
  private final TagRepository tagRepo;
  private final CourseAndProfessionRepository capRefRepo;
  private final CourseAndTagRepository catRefRepo;

  @Override
  @Transactional
  public CourseCreateUpdateDto createCourse(CourseCreateUpdateDto dto) {
    CourseProvider provider = providerRepo.findById(dto.getProviderId())
        .orElseThrow(() -> new ServiceException(COURSE_PROVIDER_NOT_FOUND));
    Course course = saveNewCourseAndFlush(courseMapper.toEntity(dto), provider);

    Profession profession = professionRepo.findById(dto.getProfessionId())
        .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));
    saveNewCapRef(course, profession);

    saveNewTagsAndSaveNewCatRefs(course, dto.getTags());

    CourseCreateUpdateDto createdCourse = courseMapper.toDto(course);
    createdCourse.setProfessionId(dto.getProfessionId());
    createdCourse.setTags(dto.getTags());
    return createdCourse;
  }

  @Override
  @Transactional
  public CourseReadDto getCourseById(long id) {
    Course course = courseRepo.findById(id)
        .orElseThrow(() -> new ServiceException(COURSE_NOT_FOUND));
    return toReadCourseDto(course);
  }

  @Override
  @Transactional
  public CourseCreateUpdateDto editCourseById(long id, CourseCreateUpdateDto dto) {
    CourseProvider provider = providerRepo.findById(dto.getProviderId())
        .orElseThrow(() -> new ServiceException(COURSE_PROVIDER_NOT_FOUND));
    Course course = courseRepo.findById(id)
        .orElseThrow(() -> new ServiceException(COURSE_NOT_FOUND));
    setCourseUpdates(dto, course, provider);
    Course updatedCourse = courseRepo.saveAndFlush(course);

    Profession profession = professionRepo.findById(dto.getProfessionId())
        .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));
    capRefRepo.deleteAllByCourse_Id(updatedCourse.getId());
    saveNewCapRef(updatedCourse, profession);

    catRefRepo.deleteAllByCourse_Id(updatedCourse.getId());
    saveNewTagsAndSaveNewCatRefs(updatedCourse, dto.getTags());

    CourseCreateUpdateDto updatedDto = courseMapper.toDto(updatedCourse);
    updatedDto.setProfessionId(dto.getProfessionId());
    updatedDto.setTags(dto.getTags());
    return updatedDto;
  }

  @Override
  @Transactional
  public List<CourseReadDto> getAllCourses() {
    return courseRepo.findAll()
        .stream().map(this::toReadCourseDto).toList();
  }

  @Override
  @Transactional
  public CourseReadPageDto getFilteredCoursePage(
      int pageNum, String sortBy, FilterParamsDto filter) {

    --pageNum;
    if (pageNum < 0) {
      throw new ServiceException(NEGATIVE_PAGE_NUMBER);
    }

    Sort sort;
    if (sortBy == null) {
      sort = Sort.by(Sort.Order.asc("startsAt"));
    } else if (sortBy.equals("startsAt")) {
      sort = Sort.by(Sort.Order.asc("startsAt"));
    } else if (sortBy.equals("endsAt")) {
      sort = Sort.by(Sort.Order.asc("endsAt"));
    } else {
      sort = Sort.by(Sort.Order.asc("startsAt"));
    }

    Pageable pageable = PageRequest.of(pageNum, props.getDefaultPageSize(), sort);

    return findPagedFilteredCourses(filter, pageable);
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

  private void setCourseUpdates(
      CourseCreateUpdateDto dto, Course course, CourseProvider provider) {

    course.setProvider(provider);
    course.setTitle(dto.getTitle());
    course.setUrl(dto.getUrl());
    course.setCoverUrl(dto.getCoverUrl());
    course.setDescription(dto.getDescription());
    course.setStartsAt(dto.getStartsAt());
    course.setEndsAt(dto.getEndsAt());
    course.setIsAdvanced(dto.getIsAdvanced());
  }

  private CourseReadPageDto findPagedFilteredCourses(FilterParamsDto filter, Pageable pageable) {
    String loweredSearch = (filter.getSearch() == null)
        ? ""
        : filter.getSearch().toLowerCase();
    filter.setSearch(loweredSearch);

    Page<Course> page = courseRepo.findByFilter(
        filter.getSearch(),
        filter.getIsAdvanced(),
        filter.getProfessionId(),
        filter.getStartsAt(),
        filter.getEndsAt(),
        pageable);

    return toReadCoursePageDto(page);
  }

  private CourseReadDto toReadCourseDto(Course course) {
    Profession courseProfession = course.getCourseAndProfessionRefs()
        .iterator().next()
        .getProfession();

    List<String> tags = course.getCourseAndTagRefs().stream()
        .map(cat -> cat.getTag().getValue())
        .toList();

    CourseReadDto courseReadDto = courseMapper.toReadCourseDto(course);
    courseReadDto.setProfessionId(courseProfession.getId());
    courseReadDto.setProfessionName(courseProfession.getName());
    courseReadDto.setTags(tags);

    return courseReadDto;
  }

  private CourseReadPageDto toReadCoursePageDto(Page<Course> page) {
    CourseReadPageDto pageDto = new CourseReadPageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(this::toReadCourseDto).toList());
    return pageDto;
  }

}
