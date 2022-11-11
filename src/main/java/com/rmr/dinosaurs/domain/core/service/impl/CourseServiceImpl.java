package com.rmr.dinosaurs.domain.core.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseErrorCode.COURSE_END_DATE_BEFORE_OR_EQUALS_START_DATE;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseErrorCode.COURSE_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseProviderErrorCode.COURSE_PROVIDER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseStudyErrorCode.COURSE_WITH_USER_INFO_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseStudyErrorCode.DUPLICATE_STUDY_ERROR;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseStudyErrorCode.END_DATE_TIME_ERROR;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseStudyErrorCode.USER_ROLE_ERROR;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.PageErrorCode.NEGATIVE_PAGE_NUMBER;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ReviewErrorCode.DOUBLE_VOTE_ERROR;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_MODERATOR;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.core.configuration.properties.CourseServiceProperties;
import com.rmr.dinosaurs.domain.core.configuration.properties.CourseStudyServiceProperties;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.core.model.CourseAndProfession;
import com.rmr.dinosaurs.domain.core.model.CourseAndTag;
import com.rmr.dinosaurs.domain.core.model.CourseProvider;
import com.rmr.dinosaurs.domain.core.model.CourseStudy;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.Review;
import com.rmr.dinosaurs.domain.core.model.Tag;
import com.rmr.dinosaurs.domain.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.FilterParamsDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyInfoResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.FilterCourseStudyParamsDto;
import com.rmr.dinosaurs.domain.core.service.CourseService;
import com.rmr.dinosaurs.domain.core.utils.converter.CourseStudyPdfExporter;
import com.rmr.dinosaurs.domain.core.utils.mapper.CourseEntityDtoMapper;
import com.rmr.dinosaurs.domain.core.utils.mapper.CourseStudyDtoMapper;
import com.rmr.dinosaurs.domain.core.utils.mapper.ReviewEntityDtoMapper;
import com.rmr.dinosaurs.domain.notification.client.NotificationClient;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseAndProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseAndTagRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseStudyRepository;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.ReviewRepository;
import com.rmr.dinosaurs.infrastucture.database.core.TagRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseServiceProperties props;
  private final CourseStudyServiceProperties courseStudyServiceProp;
  private final CourseEntityDtoMapper courseMapper;
  private final ReviewEntityDtoMapper reviewEntityDtoMapper;
  private final CourseStudyDtoMapper courseStudyDtoMapper;
  private final NotificationClient notificationClient;

  private final CourseRepository courseRepo;
  private final CourseProviderRepository providerRepo;
  private final ProfessionRepository professionRepo;
  private final TagRepository tagRepo;
  private final CourseAndProfessionRepository capRefRepo;
  private final CourseAndTagRepository catRefRepo;
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final ReviewRepository reviewRepository;
  private final CourseStudyRepository courseStudyRepository;

  private final CourseStudyPdfExporter courseStudyPdfExporter;

  @Override
  @Transactional
  public CourseCreateUpdateDto addCourse(CourseCreateUpdateDto dto) {
    CourseProvider provider = providerRepo.findById(dto.getProviderId())
        .orElseThrow(() -> new ServiceException(COURSE_PROVIDER_NOT_FOUND));
    Course course = saveNewCourseAndFlush(courseMapper.toEntity(dto), provider);

    Profession profession = professionRepo.findById(dto.getProfessionId())
        .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));
    saveNewCapRef(course, profession);

    if (!dto.getEndsAt().isAfter(dto.getStartsAt())) {
      throw new ServiceException(COURSE_END_DATE_BEFORE_OR_EQUALS_START_DATE);
    }

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
    if (!dto.getEndsAt().isAfter(dto.getStartsAt())) {
      throw new ServiceException(COURSE_END_DATE_BEFORE_OR_EQUALS_START_DATE);
    }
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
  public CourseStudyReadPageDto getFilteredCourseInformationPage(int pageNum,
      FilterCourseStudyParamsDto filter) {

    --pageNum;
    if (pageNum < 0) {
      throw new ServiceException(NEGATIVE_PAGE_NUMBER);
    }

    Sort sort = Sort.by(Sort.Order.asc("course.title"), Sort.Order.desc("score"));
    Pageable pageable = PageRequest.of(pageNum, courseStudyServiceProp.getDefaultPageSize(),
        sort);
    setDefaultFilterPropsForCourseStudy(filter);
    return findPagedFilteredCourseStudy(filter, pageable);
  }

  @Override
  @Transactional
  public void exportFilteredCourseInformationToPdf(FilterCourseStudyParamsDto filter,
      HttpServletResponse response) {
    Sort sort = Sort.by(Sort.Order.asc("course.title"), Sort.Order.desc("score"));
    Pageable pageable = PageRequest.of(0, courseStudyServiceProp.getMaxCourseStudyRowsInPdf(),
        sort);
    setDefaultFilterPropsForCourseStudy(filter);
    List<CourseStudyInfoResponseDto> studyInfoResponseDtoList = findPagedFilteredCourseStudy(filter,
        pageable).getContent();
    courseStudyPdfExporter.export(response, studyInfoResponseDtoList);

  }

  @Override
  @Transactional
  public ReviewResponseDto addReview(Long courseId, ReviewCreateDto reviewDto,
      Principal principal) {
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
    // count new average rating
    long newQuantityOfVotes = course.getVotes() + 1L;
    Double sumOfAllRatings = course.getVotes() * course.getAverageRating();
    Double newAverageRating = (sumOfAllRatings + review.getRating()) / newQuantityOfVotes;
    course.setVotes(newQuantityOfVotes);
    course.setAverageRating(newAverageRating);

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

  @Override
  @Transactional
  public CourseStudyResponseDto createCourseStudy(Principal principal, Long courseId,
      CourseStudyCreateDto courseStudyCreateDto) {
    User user = userRepository.findByEmailIgnoreCase(principal.getName())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    if (!user.getRole().equals(Authority.ROLE_REGULAR)) {
      throw new ServiceException(USER_ROLE_ERROR);
    }
    UserInfo userInfo = userInfoRepository.findByUser(user)
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    Course course = courseRepo.findById(courseId)
        .orElseThrow(() -> new ServiceException(COURSE_NOT_FOUND));
    List<String> professions = getProfessionsList(course);
    Optional<CourseStudy> optionalCourseStudy = courseStudyRepository.findByCourseIdAndUserInfoId(
        courseId, userInfo.getId());
    if (optionalCourseStudy.isPresent()) {
      throw new ServiceException(DUPLICATE_STUDY_ERROR);
    }
    CourseStudy courseStudy = courseStudyDtoMapper.toEntity(courseStudyCreateDto, userInfo,
        course);
    CourseStudy createdCourseStudy = courseStudyRepository.saveAndFlush(courseStudy);

    return courseStudyDtoMapper.toCourseStudyResponseDto(createdCourseStudy, professions);
  }

  @Override
  @Transactional
  public List<CourseStudyResponseDto> getMyCourseStudy(Principal principal) {
    User user = userRepository.findByEmailIgnoreCase(principal.getName())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    if (!user.getRole().equals(Authority.ROLE_REGULAR)) {
      throw new ServiceException(USER_ROLE_ERROR);
    }
    UserInfo userInfo = userInfoRepository.findByUser(user)
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    return courseStudyRepository.findByUserInfoId(userInfo.getId())
        .stream()
        .map(cs -> courseStudyDtoMapper.toCourseStudyResponseDto(cs,
            getProfessionsList(cs.getCourse())))
        .toList();
  }

  @Override
  @Transactional
  public void finishCourseStudy(Long courseId,
      CourseStudyUpdateDto courseStudyUpdateDto) {
    User user = userRepository.findByEmailIgnoreCase(courseStudyUpdateDto.userEmail())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    if (!user.getRole().equals(Authority.ROLE_REGULAR)) {
      throw new ServiceException(USER_ROLE_ERROR);
    }
    UserInfo userInfo = userInfoRepository.findByUser(user)
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    CourseStudy courseStudy = courseStudyRepository.findByCourseIdAndUserInfoId(courseId,
            userInfo.getId())
        .orElseThrow(() -> new ServiceException(COURSE_WITH_USER_INFO_NOT_FOUND));
    if (courseStudyUpdateDto.endsAt().isBefore(courseStudy.getStartsAt())) {
      throw new ServiceException(END_DATE_TIME_ERROR);
    }
    courseStudy.setEndsAt(courseStudyUpdateDto.endsAt());
    courseStudy.setScore(courseStudyUpdateDto.score());
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

  private CourseStudyReadPageDto findPagedFilteredCourseStudy(FilterCourseStudyParamsDto filter,
      Pageable pageable) {
    String loweredCourseTitle = (filter.getCourseTitle() == null)
        ? ""
        : filter.getCourseTitle().toLowerCase();
    filter.setCourseTitle(loweredCourseTitle);

    String loweredProfession = (filter.getProfession() == null)
        ? ""
        : filter.getProfession().toLowerCase();
    filter.setProfession(loweredProfession);

    Page<CourseStudy> page = courseStudyRepository.findByFilter(
        filter.getCourseTitle(),
        filter.getProfession(),
        filter.getScore(),
        filter.getEndsAt(),
        filter.getIsFinished(),
        pageable);

    return toCourseStudyReadPageDto(page);
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

  private CourseStudyReadPageDto toCourseStudyReadPageDto(Page<CourseStudy> page) {
    CourseStudyReadPageDto pageDto = new CourseStudyReadPageDto();
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setPageSize(page.getSize());
    pageDto.setPageNumber(page.getNumber() + 1);
    pageDto.setContent(page.getContent().stream()
        .map(this::toCourseStudyInfoResponseDto).toList());
    return pageDto;
  }

  private CourseStudyInfoResponseDto toCourseStudyInfoResponseDto(CourseStudy courseStudy) {
    Set<String> professions = professionRepo.findByCourseId(courseStudy.getCourse().getId())
        .stream()
        .map(Profession::getName)
        .collect(Collectors.toSet());
    return CourseStudyInfoResponseDto.builder()
        .userInfoNameAndSurname(courseStudy.getUserInfo().getName() + " "
            + courseStudy.getUserInfo().getSurname())
        .email(courseStudy.getUserInfo().getUser().getEmail())
        .courseTitle(courseStudy.getCourse().getTitle())
        .isCourseFinished(courseStudy.getEndsAt() != null)
        .finishedAt(courseStudy.getEndsAt())
        .score(courseStudy.getScore())
        .professions(professions)
        .build();
  }

  @SuppressWarnings("java:S3864")
  @Scheduled(cron = "0 0 18 * * *")
  void notifyModeratorsAboutEndingTodayCoursesAndSetCoursesAsArchived() {
    int today = LocalDateTime.now().getDayOfMonth();
    var expiringCourses = courseRepo.findAllByIsArchivedIsFalse()
        .stream()
        .filter(course -> today == course.getEndsAt().getDayOfMonth())
        .peek(course -> course.setIsArchived(true))
        .toList();
    if (!expiringCourses.isEmpty()) {
      courseRepo.saveAll(expiringCourses);
      var moderatorsEmails = getAllModeratorsEmails();
      notificationClient.endedTodayCoursesNotification(expiringCourses, moderatorsEmails);
    }
  }

  @Scheduled(cron = "0 0 7 * * *")
  void notifyModeratorsAboutCoursesWithInvalidLink() {
    var unreachableCourses = courseRepo.findAllByIsArchivedIsFalse()
        .parallelStream()
        .filter(course -> !isLinkReachable(course.getUrl()))
        .toList();
    if (!unreachableCourses.isEmpty()) {
      var moderatorsEmails = getAllModeratorsEmails();
      notificationClient.invalidCoursesLinksNotification(unreachableCourses, moderatorsEmails);
    }
  }

  private List<String> getAllModeratorsEmails() {
    return userRepository.findAllByIsConfirmedTrue().stream()
        .filter(user -> ROLE_MODERATOR.equals(user.getRole()))
        .map(User::getEmail)
        .toList();
  }

  private boolean isLinkReachable(String url) {
    try {
      URL link = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) link.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(2000);
      connection.connect();
      return 200 == connection.getResponseCode();
    } catch (IOException e) {
      log.info("Something goes wrong with link checking: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private List<String> getProfessionsList(Course course) {
    return course.getCourseAndProfessionRefs()
        .stream()
        .map(c -> c.getProfession().getName())
        .toList();
  }

  private void setDefaultFilterPropsForCourseStudy(FilterCourseStudyParamsDto filter) {
    if (filter.getScore() == null) {
      filter.setScore(courseStudyServiceProp.getDefaultScore());
    }
    if (filter.getEndsAt() == null) {
      filter.setEndsAt(courseStudyServiceProp.getDefaultEndsAt());
    }
  }

}
