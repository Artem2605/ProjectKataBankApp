package com.bank.history.service;

import com.bank.common.exception.ValidationException;
import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.mapper.HistoryMapper;
import com.bank.history.repository.HistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceImplTest {

    @Mock
    HistoryMapper mapper;

    @Mock
    HistoryRepository repository;

    @InjectMocks
    HistoryServiceImpl service;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void readByIdPositiveTest() {
        HistoryDto expected = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryEntity historyEntity = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        Mockito.doReturn(Optional.of(historyEntity)).when(repository).findById(1L);
        Mockito.doReturn(expected).when(mapper).toDto(historyEntity);

        HistoryDto actual = service.readById(1L);

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository).findById(1L);
        Mockito.verify(mapper).toDto(historyEntity);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(-1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readById(-1L));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("поиск по списку из id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        List<HistoryDto> expectedListOfDto = new ArrayList<>();
        expectedListOfDto.add(validHistoryDto);
        HistoryEntity validHistoryEntity = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        List<HistoryEntity> expectedListOfEntity = new ArrayList<>();
        expectedListOfEntity.add(validHistoryEntity);
        Mockito.doReturn(expectedListOfEntity).when(repository).findAllById(List.of(1L));
        Mockito.doReturn(expectedListOfDto).when(mapper).toListDto(expectedListOfEntity);

        List<HistoryDto> actual = service.readAllById(List.of(1L));

        Assertions.assertEquals(expectedListOfDto, actual);
        Mockito.verify(repository).findAllById(List.of(1L));
        Mockito.verify(mapper).toListDto(expectedListOfEntity);
    }

    @Test
    @DisplayName("поиск по списку из несуществующих id, негативный сценарий")
    void readAllByNonExistIdIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findAllById(List.of(1L));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readAllById(List.of(1L)));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void createValidPositiveTest() {
        HistoryDto expectedHistoryDto = new HistoryDto(1L, 2L, 2L,
                2L, 2L, 2L, 2L);
        HistoryEntity expectedHistoryEntity = new HistoryEntity(1L, 2L, 2L,
                2L, 2L, 2L, 2L);
        Mockito.doReturn(expectedHistoryEntity).when(mapper).toEntity(expectedHistoryDto);
        Mockito.doReturn(expectedHistoryEntity).when(repository).save(expectedHistoryEntity);
        Mockito.doReturn(expectedHistoryDto).when(mapper).toDto(expectedHistoryEntity);

        HistoryDto actual = service.create(expectedHistoryDto);

        Assertions.assertEquals(expectedHistoryDto, actual);
        Mockito.verify(mapper).toEntity(expectedHistoryDto);
        Mockito.verify(repository).save(expectedHistoryEntity);
        Mockito.verify(mapper).toDto(expectedHistoryEntity);
    }

    @Test
    @DisplayName("сохранение с не валидными полями, негативный сценарий")
    void createByInvalidFieldsNegativeTest() {
        HistoryDto inValidHistoryDto = new HistoryDto(-1L, -1L, -1L,
                -1L, -1L, -1L, -1L);
        Mockito.doThrow(new ValidationException("Invalid fields"))
                .when(mapper).toEntity(inValidHistoryDto);

        Assertions.assertThrows(ValidationException.class, () -> service.create(inValidHistoryDto));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        HistoryEntity validHistoryEntity = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryDto expectedHistoryDto = new HistoryDto(1L, 2L, 2L,
                2L, 2L, 2L, 2L);
        HistoryEntity expectedHistoryEntity = new HistoryEntity(1L, 2L, 2L,
                2L, 2L, 2L, 2L);
        Long validId = 1L;
        Mockito.doReturn(Optional.of(validHistoryEntity)).when(repository).findById(validId);
        Mockito.doReturn(expectedHistoryEntity).when(mapper).mergeToEntity(expectedHistoryDto, validHistoryEntity);
        Mockito.doReturn(expectedHistoryEntity).when(repository).save(expectedHistoryEntity);
        Mockito.doReturn(expectedHistoryDto).when(mapper).toDto(expectedHistoryEntity);

        HistoryDto actual = service.update(validId, expectedHistoryDto);

        Assertions.assertEquals(actual.getId(), validId);
        Assertions.assertEquals(expectedHistoryDto, actual);
        Mockito.verify(repository).findById(validId);
        Mockito.verify(mapper).mergeToEntity(expectedHistoryDto, validHistoryEntity);
        Mockito.verify(repository).save(expectedHistoryEntity);
        Mockito.verify(mapper).toDto(expectedHistoryEntity);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        Long inValidId = 2L;
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(inValidId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(inValidId, validHistoryDto));
        Mockito.verifyNoMoreInteractions(mapper, repository);
    }
}
