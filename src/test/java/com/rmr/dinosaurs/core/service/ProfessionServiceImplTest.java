package com.rmr.dinosaurs.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.rmr.dinosaurs.core.configuration.properties.ProfessionServiceProperties;
import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.dto.profession.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.profession.ProfessionPageDto;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
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

  private final Long ID = 1L;
  private final Long NOT_EXISTING_ID = -1L;
  private final String NAME = "Программист";
  private final String COVER_URL =
      "http://www.kremlinrus.ru/upload/iblock/e6f/programmist-3.jpg";
  private final String DESCRIPTION = "Привет, Мир!";
  private final String UPDATED_NAME = "Программист с опечаткой";
  private final String UPDATED_COVER_URL =
      "https://shwanoff.ru/wp-content/uploads/2018/07/hackerman.png";
  private final String UPDATED_DESCRIPTION = "Привет, Мир! с опечаткой";
  private final int DEFAULT_PAGE_SIZE = 10;
  private final int PAGE_NUM_1 = 1;
  private final Profession e = new Profession(
      ID, NAME, COVER_URL,
      null, DESCRIPTION,
      null, null, null);
  private final ProfessionDto eDto = new ProfessionDto(
      e.getId(), e.getName(), e.getCoverUrl(),
      e.getDescription()
  );
  private final ProfessionDto updatedProfessionDto = new ProfessionDto(
      e.getId(), UPDATED_NAME, UPDATED_COVER_URL,
      UPDATED_DESCRIPTION
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
    given(repoMock.findById(e.getId())).willReturn(Optional.of(e));
    given(mapperMock.toDto(e)).willReturn(eDto);

    // when
    ProfessionDto a = service.getProfessionById(e.getId());

    // then
    assertEquals(e.getId(), a.getId());
    assertEquals(e.getName(), a.getName());
    assertEquals(e.getCoverUrl(), a.getCoverUrl());
    assertEquals(e.getDescription(), a.getDescription());
  }

  @Test
  void getProfessionById_with_not_existing_id_throws_ProfessionNotFoundException() {
    // given
    given(repoMock.findById(NOT_EXISTING_ID)).willReturn(Optional.empty());

    // when and then
    assertThrows(ProfessionNotFoundException.class,
        () -> service.getProfessionById(NOT_EXISTING_ID));
  }

  @Test
  void updateProfessionByIdById_with_existing_profession_and_its_id_returns() {
    // given
    given(repoMock.findById(e.getId())).willReturn(Optional.of(e));
    given(repoMock.saveAndFlush(e)).willReturn(e);
    //
    Profession updatedProfession = new Profession(ID, NAME, COVER_URL,
        null, DESCRIPTION,
        null, null, null);
    updatedProfession.setName(UPDATED_NAME);
    updatedProfession.setCoverUrl(UPDATED_COVER_URL);
    updatedProfession.setDescription(UPDATED_DESCRIPTION);
    given(mapperMock.toDto(updatedProfession)).willReturn(updatedProfessionDto);

    // when
    ProfessionDto a = service.updateProfessionById(e.getId(), updatedProfessionDto);

    // then
    assertEquals(e.getId(), a.getId());
    assertEquals(e.getName(), a.getName());
    assertEquals(e.getCoverUrl(), a.getCoverUrl());
    assertEquals(e.getDescription(), a.getDescription());
  }

  @Test
  void updateProfessionByIdById_with_not_existing_id_throws_ProfessionNotFoundException() {
    // given
    given(repoMock.findById(NOT_EXISTING_ID)).willReturn(Optional.empty());

    // when and then
    assertThrows(ProfessionNotFoundException.class,
        () -> service.updateProfessionById(NOT_EXISTING_ID, updatedProfessionDto));
  }

  @Test
  void getAllProfessions_with_existing_profession_and_its_id_returns() {
    // given
    given(repoMock.findAll()).willReturn(List.of(e));
    given(mapperMock.toDto(e)).willReturn(eDto);

    // when
    List<ProfessionDto> a = service.getAllProfessions();

    // then
    assertFalse(a.isEmpty());
    assertEquals(e.getId(), a.get(0).getId());
    assertEquals(e.getName(), a.get(0).getName());
    assertEquals(e.getCoverUrl(), a.get(0).getCoverUrl());
    assertEquals(e.getDescription(), a.get(0).getDescription());
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
    given(propsMock.getDefaultPageSize()).willReturn(DEFAULT_PAGE_SIZE);
    given(mapperMock.toDto(e)).willReturn(eDto);
    //
    Pageable pageable = PageRequest.of(PAGE_NUM_1 - 1, DEFAULT_PAGE_SIZE);
    List<Profession> professionList = List.of(e);
    given(repoMock.findByOrderByNameAsc(pageable))
        .willReturn(new PageImpl<>(professionList));
    //
    List<ProfessionDto> professionDtoList = List.of(eDto);
    ProfessionPageDto expectedPageDto = new ProfessionPageDto(
        (long) professionList.size(), PAGE_NUM_1, professionList.size(), PAGE_NUM_1,
        professionDtoList);

    // when
    ProfessionPageDto a = service.getProfessionPage(PAGE_NUM_1);

    // then
    assertEquals(expectedPageDto.getTotalElements(), a.getTotalElements());
    assertEquals(expectedPageDto.getTotalPages(), a.getTotalPages());
    assertEquals(expectedPageDto.getPageSize(), a.getPageSize());
    assertEquals(expectedPageDto.getPageNumber(), a.getPageNumber());
    assertEquals(expectedPageDto.getContent(), a.getContent());
  }

}
