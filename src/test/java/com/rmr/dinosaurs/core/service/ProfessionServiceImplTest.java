package com.rmr.dinosaurs.core.service;

import static com.rmr.dinosaurs.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.rmr.dinosaurs.core.configuration.properties.ProfessionServiceProperties;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.ProfessionPageDto;
import com.rmr.dinosaurs.core.service.impl.ProfessionServiceImpl;
import com.rmr.dinosaurs.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProfessionServiceImplTest {

  private final Long id = 1L;
  private final Long notExistingId = -1L;
  private final String name = "Программист";
  private final String coverUrl =
      "http://www.kremlinrus.ru/upload/iblock/e6f/programmist-3.jpg";
  private final String description = "Привет, Мир!";
  private final String updatedName = "Программист с опечаткой";
  private final String updatedCoverUrl =
      "https://shwanoff.ru/wp-content/uploads/2018/07/hackerman.png";
  private final String updatedDescription = "Привет, Мир! с опечаткой";
  private final int defaultPageSize = 10;
  private final int pageNum = 1;
  private final Profession expectedProfession = new Profession(
      id, name, coverUrl,
      null, description,
      null, null, null);
  private final ProfessionDto expectedProfessionDto = new ProfessionDto(
      expectedProfession.getId(), expectedProfession.getName(), expectedProfession.getCoverUrl(),
      expectedProfession.getDescription()
  );
  private final ProfessionDto updatedProfessionDto = new ProfessionDto(
      expectedProfession.getId(), updatedName, updatedCoverUrl,
      updatedDescription
  );

  @Mock
  private ProfessionServiceProperties propsMock;

  @Mock
  private ProfessionEntityDtoMapper mapperMock;

  @Mock
  private ProfessionRepository repoMock;

  @InjectMocks
  private ProfessionServiceImpl service;

  @Test
  void getProfessionById_with_existing_profession_and_its_id_returns() {
    // given
    given(repoMock.findById(expectedProfession.getId()))
        .willReturn(Optional.of(expectedProfession));
    given(mapperMock.toDto(expectedProfession))
        .willReturn(expectedProfessionDto);

    // when
    ProfessionDto a = service.getProfessionById(expectedProfession.getId());

    // then
    assertEquals(expectedProfession.getId(), a.getId());
    assertEquals(expectedProfession.getName(), a.getName());
    assertEquals(expectedProfession.getCoverUrl(), a.getCoverUrl());
    assertEquals(expectedProfession.getDescription(), a.getDescription());
  }

  @Test
  void getProfessionById_with_not_existing_id_throws_ProfessionNotFoundException() {
    // given
    given(repoMock.findById(notExistingId)).willReturn(Optional.empty());

    assertThatThrownBy(
        // when
        () -> service.getProfessionById(notExistingId))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", PROFESSION_NOT_FOUND).hasNoCause();
  }

  @Test
  void updateProfessionByIdById_with_existing_profession_and_its_id_returns() {
    // given
    given(repoMock.findById(expectedProfession.getId()))
        .willReturn(Optional.of(expectedProfession));
    given(repoMock.saveAndFlush(expectedProfession))
        .willReturn(expectedProfession);
    //
    Profession updatedProfession = new Profession(id, name, coverUrl,
        null, description,
        null, null, null);
    updatedProfession.setName(updatedName);
    updatedProfession.setCoverUrl(updatedCoverUrl);
    updatedProfession.setDescription(updatedDescription);
    given(mapperMock.toDto(updatedProfession))
        .willReturn(updatedProfessionDto);

    // when
    ProfessionDto a = service.editProfessionById(
        expectedProfession.getId(), updatedProfessionDto
    );

    // then
    assertEquals(expectedProfession.getId(), a.getId());
    assertEquals(expectedProfession.getName(), a.getName());
    assertEquals(expectedProfession.getCoverUrl(), a.getCoverUrl());
    assertEquals(expectedProfession.getDescription(), a.getDescription());
  }

  @Test
  void updateProfessionByIdById_with_not_existing_id_throws_ProfessionNotFoundException() {
    // given
    given(repoMock.findById(notExistingId)).willReturn(Optional.empty());

    assertThatThrownBy(
        // when
        () -> service.editProfessionById(notExistingId, updatedProfessionDto))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", PROFESSION_NOT_FOUND).hasNoCause();
  }

  @Test
  void getAllProfessions_with_existing_profession_and_its_id_returns() {
    // given
    given(repoMock.findAll()).willReturn(List.of(expectedProfession));
    given(mapperMock.toDto(expectedProfession)).willReturn(expectedProfessionDto);

    // when
    List<ProfessionDto> a = service.getAllProfessions();

    // then
    assertFalse(a.isEmpty());
    assertEquals(expectedProfession.getId(), a.get(0).getId());
    assertEquals(expectedProfession.getName(), a.get(0).getName());
    assertEquals(expectedProfession.getCoverUrl(), a.get(0).getCoverUrl());
    assertEquals(expectedProfession.getDescription(), a.get(0).getDescription());
  }

  @Test
  void getAllProfessions_returns_empty() {
    // given
    given(repoMock.findAll()).willReturn(List.of());

    // when
    List<ProfessionDto> a = service.getAllProfessions();

    // then
    assertTrue(a.isEmpty());
  }

  @Test
  void getProfessionPage_with_existing_profession_and_its_id_and_with_pageNum_1_returns() {
    // given
    given(propsMock.getDefaultPageSize()).willReturn(defaultPageSize);
    given(mapperMock.toDto(expectedProfession)).willReturn(expectedProfessionDto);
    //
    Pageable pageable = PageRequest.of(pageNum - 1, defaultPageSize);
    List<Profession> professionList = List.of(expectedProfession);
    given(repoMock.findByOrderByNameAsc(pageable))
        .willReturn(new PageImpl<>(professionList));
    //
    List<ProfessionDto> professionDtoList = List.of(expectedProfessionDto);
    ProfessionPageDto expectedPageDto = new ProfessionPageDto(
        (long) professionList.size(), pageNum, professionList.size(), pageNum,
        professionDtoList);

    // when
    ProfessionPageDto a = service.getProfessionPage(pageNum);

    // then
    assertEquals(expectedPageDto.getTotalElements(), a.getTotalElements());
    assertEquals(expectedPageDto.getTotalPages(), a.getTotalPages());
    assertEquals(expectedPageDto.getPageSize(), a.getPageSize());
    assertEquals(expectedPageDto.getPageNumber(), a.getPageNumber());
    assertEquals(expectedPageDto.getContent(), a.getContent());
  }

}
