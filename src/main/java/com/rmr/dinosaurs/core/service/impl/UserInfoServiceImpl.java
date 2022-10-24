package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.exception.errorcode.UserInfoErrorCode.NO_PERMISSIONS_TO_EDIT;
import static com.rmr.dinosaurs.core.exception.errorcode.UserInfoErrorCode.USER_INFO_NOT_FOUND;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.core.service.impl.UserServiceImpl.NO_USER_FOUND_EXCEPTION_SUPPLIER;

import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.service.UserInfoService;
import com.rmr.dinosaurs.core.utils.converters.UserInfoConverter;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  public static final Supplier<RuntimeException> USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(USER_INFO_NOT_FOUND);

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
    if (Objects.nonNull(userInfoDto.getName())) {
      currentUserInfo.setName(userInfoDto.getName());
    }
    if (Objects.nonNull(userInfoDto.getSurname())) {
      currentUserInfo.setSurname(userInfoDto.getSurname());
    }
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
        .filter(userInfo -> ROLE_MODERATOR.equals(userInfo.getUser().getRole()))
        .map(userInfoConverter::toShortUserInfoDto).toList();
  }

  private void checkUserCanEditProfileOrThrow(UserInfoDto userInfoDto, UserInfo currentUserInfo) {
    if (Objects.isNull(userInfoDto.getUserId())
        || !userInfoDto.getUserId().equals(currentUserInfo.getUser().getId())) {
      throw new ServiceException(NO_PERMISSIONS_TO_EDIT);
    }
  }

  private UserInfo getUserInfoFromRepositoryByUserId(Long id) {
    User user = userRepository.findById(id).orElseThrow(NO_USER_FOUND_EXCEPTION_SUPPLIER);

    return userInfoRepository.findByUser(user)
        .orElseThrow(USER_PROFILE_NOT_FOUND_EXCEPTION_SUPPLIER);
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
