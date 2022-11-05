package com.rmr.dinosaurs.domain.userinfo.service.impl;

import static com.rmr.dinosaurs.domain.auth.service.impl.UserServiceImpl.NO_USER_FOUND_EXCEPTION_SUPPLIER;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static com.rmr.dinosaurs.domain.userinfo.exception.errorcode.UserInfoErrorCode.NO_PERMISSIONS_TO_DELETE;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.userinfo.exception.errorcode.UserInfoErrorCode;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.service.UserInfoService;
import com.rmr.dinosaurs.domain.userinfo.utils.converter.UserInfoConverter;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  public static final Supplier<RuntimeException> USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(UserInfoErrorCode.USER_INFO_NOT_FOUND);

  private final UserInfoRepository userInfoRepository;
  private final UserRepository userRepository;
  private final UserInfoConverter userInfoConverter;


  @Override
  public UserInfoDto getMyProfile() {
    var currentUserInfo = getCurrentUserInfoFromRepository();
    return userInfoConverter.toUserInfoDto(currentUserInfo);
  }

  @Override
  public UserInfoDto editMyProfile(UserInfoDto userInfoDto) {
    var currentUserInfo = getCurrentUserInfoFromRepository();
    Optional.of(userInfoDto.getName())
        .ifPresent(currentUserInfo::setName);
    Optional.of(userInfoDto.getSurname())
        .ifPresent(currentUserInfo::setSurname);
    currentUserInfo = userInfoRepository.saveAndFlush(currentUserInfo);
    return userInfoConverter.toUserInfoDto(currentUserInfo);
  }

  @Override
  public UserInfoDto getUserInfoById(Long id) {
    return userInfoConverter.toUserInfoDto(userInfoRepository.findById(id)
        .orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER));
  }

  @Override
  public List<ShortUserInfoDto> getAllUserInfos() {
    return userInfoRepository.findAll().stream().map(userInfoConverter::toShortUserInfoDto)
        .toList();
  }

  @Override
  public List<ShortUserInfoDto> getAllModerators() {
    return userInfoRepository.findAll().stream()
        .filter(userInfo -> ROLE_MODERATOR.equals(userInfo.getUser().getRole()))
        .map(userInfoConverter::toShortUserInfoDto).toList();
  }

  @Override
  public UserInfoDto deleteMyProfile() {
    var currentUser = userRepository
        .findByIdAndIsConfirmedTrueAndIsArchivedFalse(getCurrentUserPrincipal().getId())
        .orElseThrow(NO_USER_FOUND_EXCEPTION_SUPPLIER);
    if (!ROLE_REGULAR.equals(currentUser.getRole())) {
      throw new ServiceException(NO_PERMISSIONS_TO_DELETE);
    }

    currentUser.setIsArchived(true);
    currentUser.setArchivedAt(LocalDateTime.now());
    userRepository.saveAndFlush(currentUser);

    var currentUserInfo = userInfoRepository.findByUser(currentUser)
        .orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER);
    currentUserInfo.setName("Динозаврик");
    currentUserInfo.setSurname("Спрятался");
    currentUserInfo.setRecommendedProfession(null);
    currentUserInfo = userInfoRepository.saveAndFlush(currentUserInfo);
    return userInfoConverter.toUserInfoDto(currentUserInfo);
  }

  private UserInfo getCurrentUserInfoFromRepository() {
    var currentUserId = getCurrentUserPrincipal().getId();
    var currentUser = getUserFromRepositoryByUserIdOrThrow(currentUserId);

    return userInfoRepository.findByUser(currentUser)
        .orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER);
  }

  private User getUserFromRepositoryByUserIdOrThrow(Long id) {
    return userRepository.findById(id).orElseThrow(
        NO_USER_FOUND_EXCEPTION_SUPPLIER);
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
