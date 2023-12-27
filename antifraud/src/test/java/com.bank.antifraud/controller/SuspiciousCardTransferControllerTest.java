package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SuspiciousCardTransferControllerTest {
    @InjectMocks
    private SuspiciousCardTransferController suspiciousCardTransferController;
    @Mock
    private SuspiciousCardTransferService suspiciousCardTransferService;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        suspiciousCardTransferDto.setId(1L);
        suspiciousCardTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousCardTransferDto)
                .when(suspiciousCardTransferService)
                .findById(suspiciousCardTransferDto.getId());

        ResponseEntity<SuspiciousCardTransferDto> read =
                suspiciousCardTransferController.read(suspiciousCardTransferDto.getId());

        Assertions.assertEquals(read.getBody(), suspiciousCardTransferDto);
        Assertions.assertEquals(read.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Long testId = 5L;
        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousCardTransferService).findById(testId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousCardTransferController.read(testId);
        });
    }

    @Test
    @DisplayName("Чтение всех подозрительных трансферов по списку id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDtoFirst = new SuspiciousCardTransferDto();
        suspiciousCardTransferDtoFirst.setId(1L);
        suspiciousCardTransferDtoFirst.setSuspiciousReason("Test");
        SuspiciousCardTransferDto suspiciousCardTransferDtoSecond = new SuspiciousCardTransferDto();
        suspiciousCardTransferDtoSecond.setId(2L);
        suspiciousCardTransferDtoSecond.setSuspiciousReason("Test");

        List<SuspiciousCardTransferDto> suspiciousCardTransferDtos = List.of(suspiciousCardTransferDtoFirst,
                suspiciousCardTransferDtoSecond);
        List<Long> ids = List.of(suspiciousCardTransferDtoFirst.getId(), suspiciousCardTransferDtoSecond.getId());

        Mockito.doReturn(suspiciousCardTransferDtos).when(suspiciousCardTransferService).findAllById(ids);

        ResponseEntity<List<SuspiciousCardTransferDto>> listResponseEntity =
                suspiciousCardTransferController.readAll(ids);

        Assertions.assertEquals(listResponseEntity.getBody(), suspiciousCardTransferDtos);
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение всех подозрительных переводов по номеру карты по списку, имеющему несуществующие id, " +
            "негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousCardTransferService).findAllById(ids);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousCardTransferController.readAll(ids);
        });
    }

    @Test
    @DisplayName("Сохранение подозрительного перевода по номеру карты в БД, позитивный сценарий")
    void createPositiveTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        suspiciousCardTransferDto.setId(1L);
        suspiciousCardTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousCardTransferDto)
                .when(suspiciousCardTransferService)
                .save(suspiciousCardTransferDto);

        ResponseEntity<SuspiciousCardTransferDto> create =
                suspiciousCardTransferController.create(suspiciousCardTransferDto);

        Assertions.assertEquals(create.getBody(), suspiciousCardTransferDto);
        Assertions.assertEquals(create.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Сохранение null в БД, негативный сценарий")
    void createNullDtoNegativeTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        Mockito.doThrow(new NullPointerException()).when(suspiciousCardTransferService).save(suspiciousCardTransferDto);

        Assertions.assertThrows(NullPointerException.class, () -> {
            suspiciousCardTransferController.create(suspiciousCardTransferDto);
        });
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру карты в БД, позитивный сценарий")
    void updatePositiveTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        suspiciousCardTransferDto.setId(1L);
        suspiciousCardTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousCardTransferDto)
                .when(suspiciousCardTransferService)
                .update(suspiciousCardTransferDto.getId(), suspiciousCardTransferDto);

        ResponseEntity<SuspiciousCardTransferDto> update =
                suspiciousCardTransferController.update(suspiciousCardTransferDto, suspiciousCardTransferDto.getId());

        Assertions.assertEquals(update.getBody(), suspiciousCardTransferDto);
        Assertions.assertEquals(update.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру карты по несуществующему id, " +
            "негативный сценарий")
    void updateNonExistIdNegativeTest() {
        Long testId = 10L;
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        suspiciousCardTransferDto.setId(10L);
        suspiciousCardTransferDto.setSuspiciousReason("Test");

        Mockito.doThrow(new EntityNotFoundException())
                .when(suspiciousCardTransferService)
                .update(testId, suspiciousCardTransferDto);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousCardTransferController.update(suspiciousCardTransferDto, testId);
        });
    }
}
