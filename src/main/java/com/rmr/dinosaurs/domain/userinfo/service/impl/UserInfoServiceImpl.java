package com.rmr.dinosaurs.domain.userinfo.service.impl;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.service.impl.UserServiceImpl;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.userinfo.exception.errorcode.UserInfoErrorCode;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.service.UserInfoService;
import com.rmr.dinosaurs.domain.userinfo.utils.converter.UserInfoConverter;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.util.List;
import java.util.Objects;
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
    return userInfoConverter.toUserInfoDto(
        getUserInfoFromRepositoryByUserId(getCurrentUserPrincipal().getId()));
  }

  @Override
  public UserInfoDto editMyProfile(UserInfoDto userInfoDto) {
    var currentUserInfo = getUserInfoFromRepositoryByUserId(
        getCurrentUserPrincipal().getId());
    checkUserCanEditProfileOrThrow(userInfoDto, currentUserInfo);
    Optional.of(userInfoDto.getName())
        .ifPresent(currentUserInfo::setName);
    Optional.of(userInfoDto.getSurname())
        .ifPresent(currentUserInfo::setSurname);
    var savedUserInfo = userInfoRepository.save(currentUserInfo);
    return userInfoConverter.toUserInfoDto(savedUserInfo);
  }

  @Override
  public UserInfoDto getUserInfoById(Long id) {
    return userInfoConverter.toUserInfoDto(
        userInfoRepository.findById(id).orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER));
  }

  @Override
  public List<ShortUserInfoDto> getAllUserInfos() {
    return userInfoRepository.findAll().stream().map(userInfoConverter::toShortUserInfoDto)
        .toList();
  }

  @Override
  public List<ShortUserInfoDto> getAllModerators() {
    return userInfoRepository.findAll().stream()
        .filter(userInfo -> Authority.ROLE_MODERATOR.equals(userInfo.getUser().getRole()))
        .map(userInfoConverter::toShortUserInfoDto).toList();
  }

  private void checkUserCanEditProfileOrThrow(UserInfoDto userInfoDto, UserInfo currentUserInfo) {
    if (Objects.isNull(userInfoDto.getUserId())
        || !userInfoDto.getUserId().equals(currentUserInfo.getUser().getId())) {
      throw new ServiceException(UserInfoErrorCode.NO_PERMISSIONS_TO_EDIT);
    }
  }

  private UserInfo getUserInfoFromRepositoryByUserId(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        UserServiceImpl.NO_USER_FOUND_EXCEPTION_SUPPLIER);

    return userInfoRepository.findByUser(user)
        .orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER);
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
