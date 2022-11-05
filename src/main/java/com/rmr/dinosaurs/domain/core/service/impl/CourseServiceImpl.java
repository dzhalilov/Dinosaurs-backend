package com.rmr.dinosaurs.domain.core.service.impl;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.core.configuration.properties.CourseServiceProperties;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.*;
import com.rmr.dinosaurs.domain.core.model.dto.*;
import com.rmr.dinosaurs.domain.core.service.CourseService;
import com.rmr.dinosaurs.domain.core.utils.mapper.CourseEntityDtoMapper;
import com.rmr.dinosaurs.domain.core.utils.mapper.ReviewEntityDtoMapper;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.*;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseErrorCode.COURSE_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.PageErrorCode.NEGATIVE_PAGE_NUMBER;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ReviewErrorCode.DOUBLE_VOTE_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseServiceProperties props;
  private final CourseEntityDtoMapper courseMapper;
  private final ReviewEntityDtoMapper reviewEntityDtoMapper;

  private final CourseRepository courseRepo;
  private final CourseProviderRepository providerRepo;
  private final ProfessionRepository professionRepo;
  private final TagRepository tagRepo;
  private final CourseAndProfessionRepository capRefRepo;
  private final CourseAndTagRepository catRefRepo;
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final ReviewRepository reviewRepository;

  @Override
  @Transactional
  public CourseCreateUpdateDto addCourse(CourseCreateUpdateDto dto) {
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

  @Override
  @Transactional
  public ReviewResponseDto addReview(Long courseId, ReviewCreateDto reviewDto, Principal principal) {
    User user = userRepository.findByEmailIgnoreCase(principal.getName())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    UserInfo userInfo = userInfoRepository.findByUser(user)
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

    Course course = courseRepo.findById(courseId)
        .orElseThrow(() -> new ServiceException(COURSE_NOT_FOUND));
    Optional<Review> optionalReview = reviewRepository
        .findReviewByCourseAndUserInfoId(courseId, userInfo.getId());
    if (optionalReview.isPresent()) {
      throw new ServiceException(DOUBLE_VOTE_ERROR);
    }
    Review review = reviewEntityDtoMapper.toEntity(reviewDto);
    review.setCourse(course);
    review.setUserInfo(userInfo);
    if (review.getRating() != null) {
      long newQuantityOfVotes = course.getVotes() + 1L;
      Double sumOfAllRatings = course.getVotes() * course.getAverageRating();
      Double newAverageRating = (sumOfAllRatings + review.getRating()) / newQuantityOfVotes;
      course.setVotes(newQuantityOfVotes);
      course.setAverageRating(newAverageRating);
    }
    Review createdReview = reviewRepository.saveAndFlush(review);
    return reviewEntityDtoMapper.toReviewResponseDto(createdReview);
  }

  @Override
  public List<ReviewResponseDto> getReviewsByCourseId(Long courseId) {
    courseRepo.findById(courseId).orElseThrow(() -> new ServiceException(COURSE_NOT_FOUND));
    List<Review> reviewList = reviewRepository.findByCourseId(courseId);
    return reviewList.stream()
        .map(reviewEntityDtoMapper::toReviewResponseDto)
        .toList();
  }

  private Course saveNewCourseAndFlush(Course course, CourseProvider provider) {
    course.setProvider(provider);
    course.setAverageRating(5.0);
    course.setVotes(0L);

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
        LocalDateTime.now(),
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
