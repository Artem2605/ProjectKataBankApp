package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.service.SuspiciousAccountTransferService;
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
public class SuspiciousAccountTransferControllerTest {
    @InjectMocks
    private SuspiciousAccountTransferController suspiciousAccountTransferController;
    @Mock
    private SuspiciousAccountTransferService suspiciousAccountTransferService;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        SuspiciousAccountTransferDto suspiciousAccountTransferDto = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDto.setId(1L);
        suspiciousAccountTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousAccountTransferDto)
                .when(suspiciousAccountTransferService)
                .findById(suspiciousAccountTransferDto.getId());

        ResponseEntity<SuspiciousAccountTransferDto> read =
                suspiciousAccountTransferController.read(suspiciousAccountTransferDto.getId());

        Assertions.assertEquals(read.getBody(), suspiciousAccountTransferDto);
        Assertions.assertEquals(read.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Long testId = 5L;
        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousAccountTransferService).findById(testId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousAccountTransferController.read(testId);
        });
    }

    @Test
    @DisplayName("Чтение всех подозрительных переводов по номеру счета по списку id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        SuspiciousAccountTransferDto suspiciousAccountTransferDtoFirst = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDtoFirst.setId(1L);
        suspiciousAccountTransferDtoFirst.setSuspiciousReason("Test");
        SuspiciousAccountTransferDto suspiciousAccountTransferDtoSecond = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDtoSecond.setId(2L);
        suspiciousAccountTransferDtoSecond.setSuspiciousReason("Test");

        List<SuspiciousAccountTransferDto> suspiciousAccountTransferDtos = List.of(suspiciousAccountTransferDtoFirst,
                suspiciousAccountTransferDtoSecond);
        List<Long> ids = List.of(suspiciousAccountTransferDtoFirst.getId(), suspiciousAccountTransferDtoSecond.getId());

        Mockito.doReturn(suspiciousAccountTransferDtos).when(suspiciousAccountTransferService).findAllById(ids);

        ResponseEntity<List<SuspiciousAccountTransferDto>> listResponseEntity =
                suspiciousAccountTransferController.readAll(ids);

        Assertions.assertEquals(listResponseEntity.getBody(), suspiciousAccountTransferDtos);
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение всех подозрительных переводов по номеру счета по списку, имеющему несуществующие id, " +
            "негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousAccountTransferService).findAllById(ids);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousAccountTransferController.readAll(ids);
        });
    }

    @Test
    @DisplayName("Сохранение подозрительного перевода по номеру счета в БД, позитивный сценарий")
    void createPositiveTest() {
        SuspiciousAccountTransferDto suspiciousAccountTransferDto = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDto.setId(1L);
        suspiciousAccountTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousAccountTransferDto)
                .when(suspiciousAccountTransferService)
                .save(suspiciousAccountTransferDto);

        ResponseEntity<SuspiciousAccountTransferDto> create =
                suspiciousAccountTransferController.create(suspiciousAccountTransferDto);

        Assertions.assertEquals(create.getBody(), suspiciousAccountTransferDto);
        Assertions.assertEquals(create.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Сохранение null в БД, негативный сценарий")
    void createNullDtoNegativeTest() {
        SuspiciousAccountTransferDto suspiciousAccountTransferDto = new SuspiciousAccountTransferDto();
        Mockito.doThrow(new NullPointerException())
                .when(suspiciousAccountTransferService)
                .save(suspiciousAccountTransferDto);

        Assertions.assertThrows(NullPointerException.class, () -> {
            suspiciousAccountTransferController.create(suspiciousAccountTransferDto);
        });
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру счета в БД, позитивный сценарий")
    void updatePositiveTest() {
        SuspiciousAccountTransferDto suspiciousAccountTransferDto = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDto.setId(1L);
        suspiciousAccountTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousAccountTransferDto)
                .when(suspiciousAccountTransferService)
                .update(suspiciousAccountTransferDto.getId(), suspiciousAccountTransferDto);

        ResponseEntity<SuspiciousAccountTransferDto> update =
                suspiciousAccountTransferController.update(suspiciousAccountTransferDto,
                        suspiciousAccountTransferDto.getId());

        Assertions.assertEquals(update.getBody(), suspiciousAccountTransferDto);
        Assertions.assertEquals(update.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру счета по несуществующему id, " +
            "негативный сценарий")
    void updateNonExistIdNegativeTest() {
        Long testId = 10L;
        SuspiciousAccountTransferDto suspiciousAccountTransferDto = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDto.setId(10L);
        suspiciousAccountTransferDto.setSuspiciousReason("Test");

        Mockito.doThrow(new EntityNotFoundException())
                .when(suspiciousAccountTransferService)
                .update(testId, suspiciousAccountTransferDto);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousAccountTransferController.update(suspiciousAccountTransferDto, testId);
        });
    }
}
