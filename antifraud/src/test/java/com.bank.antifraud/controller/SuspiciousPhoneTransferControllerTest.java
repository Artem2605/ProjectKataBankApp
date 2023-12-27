package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.service.SuspiciousPhoneTransferService;
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
public class SuspiciousPhoneTransferControllerTest {
    @InjectMocks
    private SuspiciousPhoneTransferController suspiciousPhoneTransferController;
    @Mock
    private SuspiciousPhoneTransferService suspiciousPhoneTransferService;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDto.setId(1L);
        suspiciousPhoneTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousPhoneTransferDto)
                .when(suspiciousPhoneTransferService)
                .findById(suspiciousPhoneTransferDto.getId());

        ResponseEntity<SuspiciousPhoneTransferDto> read =
                suspiciousPhoneTransferController.read(suspiciousPhoneTransferDto.getId());

        Assertions.assertEquals(read.getBody(), suspiciousPhoneTransferDto);
        Assertions.assertEquals(read.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Long testId = 5L;
        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousPhoneTransferService).findById(testId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousPhoneTransferController.read(testId);
        });
    }

    @Test
    @DisplayName("Чтение всех подозрительных переводов по номеру телефона по списку id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDtoFirst = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDtoFirst.setId(1L);
        suspiciousPhoneTransferDtoFirst.setSuspiciousReason("Test");
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDtoSecond = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDtoSecond.setId(2L);
        suspiciousPhoneTransferDtoSecond.setSuspiciousReason("Test");

        List<SuspiciousPhoneTransferDto> suspiciousPhoneTransferDtos = List.of(suspiciousPhoneTransferDtoFirst,
                suspiciousPhoneTransferDtoSecond);
        List<Long> ids = List.of(suspiciousPhoneTransferDtoFirst.getId(), suspiciousPhoneTransferDtoSecond.getId());

        Mockito.doReturn(suspiciousPhoneTransferDtos).when(suspiciousPhoneTransferService).findAllById(ids);

        ResponseEntity<List<SuspiciousPhoneTransferDto>> listResponseEntity =
                suspiciousPhoneTransferController.readAll(ids);

        Assertions.assertEquals(listResponseEntity.getBody(), suspiciousPhoneTransferDtos);
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Чтение всех подозрительных переводов по номеру телефона по списку, имеющему несуществующие id, " +
            "негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        Mockito.doThrow(new EntityNotFoundException()).when(suspiciousPhoneTransferService).findAllById(ids);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousPhoneTransferController.readAll(ids);
        });
    }

    @Test
    @DisplayName("Сохранение подозрительного перевода по номеру телефона в БД, позитивный сценарий")
    void createPositiveTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDto.setId(1L);
        suspiciousPhoneTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousPhoneTransferDto)
                .when(suspiciousPhoneTransferService)
                .save(suspiciousPhoneTransferDto);

        ResponseEntity<SuspiciousPhoneTransferDto> create =
                suspiciousPhoneTransferController.create(suspiciousPhoneTransferDto);

        Assertions.assertEquals(create.getBody(), suspiciousPhoneTransferDto);
        Assertions.assertEquals(create.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Сохранение null в БД, негативный сценарий")
    void createNullDtoNegativeTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        Mockito.doThrow(new NullPointerException())
                .when(suspiciousPhoneTransferService)
                .save(suspiciousPhoneTransferDto);

        Assertions.assertThrows(NullPointerException.class, () -> {
            suspiciousPhoneTransferController.create(suspiciousPhoneTransferDto);
        });
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру телефона в БД, позитивный сценарий")
    void updatePositiveTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDto.setId(1L);
        suspiciousPhoneTransferDto.setSuspiciousReason("Test");
        Mockito.doReturn(suspiciousPhoneTransferDto)
                .when(suspiciousPhoneTransferService)
                .update(suspiciousPhoneTransferDto.getId(), suspiciousPhoneTransferDto);

        ResponseEntity<SuspiciousPhoneTransferDto> update =
                suspiciousPhoneTransferController.update(suspiciousPhoneTransferDto, suspiciousPhoneTransferDto.getId());

        Assertions.assertEquals(update.getBody(), suspiciousPhoneTransferDto);
        Assertions.assertEquals(update.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Обновление данных о подозрительном переводе по номеру телефона по несуществующему id, " +
            "негативный сценарий")
    void updateNonExistIdNegativeTest() {
        Long testId = 10L;
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDto.setId(10L);
        suspiciousPhoneTransferDto.setSuspiciousReason("Test");

        Mockito.doThrow(new EntityNotFoundException())
                .when(suspiciousPhoneTransferService)
                .update(testId, suspiciousPhoneTransferDto);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            suspiciousPhoneTransferController.update(suspiciousPhoneTransferDto, testId);
        });
    }
}
