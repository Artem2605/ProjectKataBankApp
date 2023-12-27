package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import com.bank.antifraud.mappers.SuspiciousAccountTransferMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransferRepository;
import com.bank.antifraud.service.impl.SuspiciousAccountTransferServiceImpl;
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
class SuspiciousAccountTransferServiceImplTest {
    @Mock
    private SuspiciousAccountTransferMapper mapper;
    @Mock
    private SuspiciousAccountTransferRepository repository;
    @InjectMocks
    private SuspiciousAccountTransferServiceImpl service;

    @Test
    @DisplayName("Сохранение подозрительного перевода по номеру счета в БД, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity entity = new SuspiciousAccountTransferEntity(1L, 2L,
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
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity entity = new SuspiciousAccountTransferEntity(1L, 2L,
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
    @DisplayName("Обновление данных подозрительного перевода по номеру счета, позитивный сценарий")
    void updatePositiveTest() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity entity = new SuspiciousAccountTransferEntity(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferDto dtoUpdated = new SuspiciousAccountTransferDto(1L, 3L,
                false, true, "", "Test1");
        SuspiciousAccountTransferEntity entityUpdated = new SuspiciousAccountTransferEntity(1L, 4L,
                false, true, "", "Test2");

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(dto.getId());
        Mockito.doReturn(entityUpdated).when(mapper).mergeToEntity(dto, entity);
        Mockito.doReturn(entityUpdated).when(repository).save(entityUpdated);
        Mockito.doReturn(dtoUpdated).when(mapper).toDto(entityUpdated);

        Assertions.assertEquals(service.update(dto.getId(), dto), dtoUpdated);
    }

    @Test
    @DisplayName("Обновление данных подозрительного перевода по номеру счета, которого нет в БД, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");

        Long wrongId = 2L;
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(wrongId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(wrongId, dto));
        Mockito.verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("Поиск всех подозрительных переводов по номеру счета по списку id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        SuspiciousAccountTransferDto dtoFirst = new SuspiciousAccountTransferDto(1L, 3L,
                false, true, "", "Test");
        SuspiciousAccountTransferDto dtoSecond = new SuspiciousAccountTransferDto(2L, 4L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity entityFirst = new SuspiciousAccountTransferEntity(1L, 3L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity entitySecond = new SuspiciousAccountTransferEntity(2L, 4L,
                false, true, "", "Test");
        List<Long> ids = List.of(entityFirst.getId(), entitySecond.getId());
        List<SuspiciousAccountTransferDto> dtoList = List.of(dtoFirst, dtoSecond);
        List<SuspiciousAccountTransferEntity> entityList = List.of(entityFirst, entitySecond);

        Mockito.doReturn(Optional.ofNullable(entityFirst)).when(repository).findById(dtoFirst.getId());
        Mockito.doReturn(Optional.ofNullable(entitySecond)).when(repository).findById(dtoSecond.getId());
        Mockito.doReturn(dtoList).when(mapper).toListDto(entityList);

        Assertions.assertEquals(service.findAllById(ids), dtoList);
    }

    @Test
    @DisplayName("Поиск всех подозрительных переводов по номеру счета по списку, имеющему несуществующий id, " +
            "негативный сценарий")
    void findAllByNonExistIdsNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(repository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L, 2L)));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verifyNoMoreInteractions(mapper);
    }
}