package com.bank.history.controller;

import com.bank.common.exception.ValidationException;
import com.bank.history.dto.HistoryDto;
import com.bank.history.service.HistoryServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HistoryControllerTest {

    @Mock
    HistoryServiceImpl service;

    @InjectMocks
    HistoryController controller;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        HistoryDto expected = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        Mockito.doReturn(expected).when(service).readById(1L);

        ResponseEntity<HistoryDto> actual = controller.read(1L);

        Assertions.assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(expected, actual.getBody());
        Mockito.verify(service).readById(1L);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(service).readById(-1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.read(-1L));
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("чтение по списку из id, позитивный сценарий")
    void readAllByListOfIdPositiveTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        List<HistoryDto> expected = new ArrayList<>();
        expected.add(validHistoryDto);
        Mockito.doReturn(expected).when(service).readAllById(List.of(1L));

        ResponseEntity<List<HistoryDto>> actual = controller.readAll(List.of(1L));

        Assertions.assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(expected, actual.getBody());
        Mockito.verify(service).readAllById(List.of(1L));
    }

    @Test
    @DisplayName("чтение по списку из несуществующих id, негативный сценарий")
    void readAllByListOfNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException())
                .when(service).readAllById(List.of(1L));

        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.readAll(List.of(1L)));
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createValidPositiveTest() {
        HistoryDto expected = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        Mockito.doReturn(expected).when(service).create(expected);

        ResponseEntity<HistoryDto> actual = controller.create(expected);

        Assertions.assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(expected, actual.getBody());
        Mockito.verify(service).create(expected);
    }

    @Test
    @DisplayName("создание с не валидными полями, негативный сценарий")
    void createByInvalidFieldsNegativeTest() {
        HistoryDto inValidHistoryDto = new HistoryDto(-1L, -1L, -1L,
                -1L, -1L, -1L, -1L);
        Mockito.doThrow(new ValidationException("Invalid fields"))
                .when(service).create(inValidHistoryDto);

        Assertions.assertThrows(ValidationException.class, () -> controller.create(inValidHistoryDto));
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryDto expected = new HistoryDto(1L, 2L, 2L,
                2L, 2L, 2L, 2L);
        Long validId = 1L;
        Mockito.doReturn(expected).when(service).update(validId, expected);

        ResponseEntity<HistoryDto> actual = controller.update(validId, expected);

        Assertions.assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(expected, actual.getBody());
        Assertions.assertNotEquals(validHistoryDto, expected);
        Mockito.verify(service).update(validId, expected);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        Long inValidId = 2L;
        Mockito.doThrow(new EntityNotFoundException())
                .when(service).update(inValidId, validHistoryDto);

        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.update(inValidId, validHistoryDto));
        Mockito.verifyNoMoreInteractions(service);
    }
}