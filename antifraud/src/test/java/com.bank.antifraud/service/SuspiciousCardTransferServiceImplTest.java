package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;
import com.bank.antifraud.mappers.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import com.bank.antifraud.service.impl.SuspiciousCardTransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SuspiciousCardTransferServiceImplTest {

    @Mock
    private SuspiciousCardTransferMapper mapper;
    @Mock
    private SuspiciousCardTransferRepository repository;
    @InjectMocks
    private SuspiciousCardTransferServiceImpl service;

    @Test
    @DisplayName("Сохранение подозрительного перевода по номеру карты в БД, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity entity = new SuspiciousCardTransferEntity(1L, 2L,
                false, true, "", "Test");

        Mockito.doReturn(entity).when(mapper).toEntity(dto);
        Mockito.doReturn(entity).when(repository).save(entity);
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        Assertions.assertEquals(service.save(dto), dto);
    }

    @Test
    @DisplayName("Сохранение null в БД, негативный сценарий")
    void saveNullNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(mapper).toEntity(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.save(null));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity entity = new SuspiciousCardTransferEntity(1L, 2L,
                false, true, "", "Test");

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        Assertions.assertEquals(service.findById(entity.getId()), dto);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Обновление данных подозрительного перевода по номеру карты, позитивный сценарий")
    void updatePositiveTest() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity entity = new SuspiciousCardTransferEntity(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferDto dtoUpdated = new SuspiciousCardTransferDto(1L, 3L,
                false, true, "", "Test1");
        SuspiciousCardTransferEntity entityUpdated = new SuspiciousCardTransferEntity(1L, 4L,
                false, true, "", "Test2");

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(dto.getId());
        Mockito.doReturn(entityUpdated).when(mapper).mergeToEntity(dto, entity);
        Mockito.doReturn(entityUpdated).when(repository).save(entityUpdated);
        Mockito.doReturn(dtoUpdated).when(mapper).toDto(entityUpdated);

        Assertions.assertEquals(service.update(dto.getId(), dto), dtoUpdated);
    }

    @Test
    @DisplayName("Обновление данных подозрительного перевода по номеру карты, которого нет в БД, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");

        Long wrongId = 2L;
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(wrongId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(wrongId, dto));
        Mockito.verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("Поиск всех подозрительных переводов по номеру карты по списку id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        SuspiciousCardTransferDto dtoFirst = new SuspiciousCardTransferDto(1L, 3L,
                false, true, "", "Test");
        SuspiciousCardTransferDto dtoSecond = new SuspiciousCardTransferDto(2L, 4L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity entityFirst = new SuspiciousCardTransferEntity(1L, 3L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity entitySecond = new SuspiciousCardTransferEntity(2L, 4L,
                false, true, "", "Test");
        List<Long> ids = List.of(entityFirst.getId(), entitySecond.getId());
        List<SuspiciousCardTransferDto> dtoList = List.of(dtoFirst, dtoSecond);
        List<SuspiciousCardTransferEntity> entityList = List.of(entityFirst, entitySecond);

        Mockito.doReturn(Optional.ofNullable(entityFirst)).when(repository).findById(dtoFirst.getId());
        Mockito.doReturn(Optional.ofNullable(entitySecond)).when(repository).findById(dtoSecond.getId());
        Mockito.doReturn(dtoList).when(mapper).toListDto(entityList);

        Assertions.assertEquals(service.findAllById(ids), dtoList);
    }

    @Test
    @DisplayName("Поиск всех подозрительных переводов по номеру карты по списку, имеющему несуществующий id, " +
            "негативный сценарий")
    void findAllByNonExistIdsNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L, 2L)));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }
}