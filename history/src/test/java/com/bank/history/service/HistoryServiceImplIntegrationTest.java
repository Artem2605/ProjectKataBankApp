package com.bank.history.service;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.repository.HistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@SpringBootTest
public class HistoryServiceImplIntegrationTest {

    @Autowired
    private HistoryServiceImpl historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @AfterEach
    void resetDb() {
        historyRepository.deleteAll();
    }

    Long createDefaultHistory() {
        HistoryEntity defaultHistory = new HistoryEntity();
        defaultHistory.setTransferAuditId(777L);
        defaultHistory.setProfileAuditId(777L);
        defaultHistory.setAccountAuditId(777L);
        defaultHistory.setAntiFraudAuditId(777L);
        defaultHistory.setPublicBankInfoAuditId(777L);
        defaultHistory.setAuthorizationAuditId(777L);
        return historyRepository.save(defaultHistory).getId();
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void readByIdPositiveTest() {
        HistoryDto actual = historyService.readById(createDefaultHistory());

        Assertions.assertEquals(777L, actual.getAccountAuditId());
        Assertions.assertEquals(777L, actual.getAntiFraudAuditId());
        Assertions.assertEquals(777L, actual.getAuthorizationAuditId());
        Assertions.assertEquals(777L, actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(777L, actual.getProfileAuditId());
        Assertions.assertEquals(777L, actual.getTransferAuditId());
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> historyService.readById(1L));
    }

    @Test
    @DisplayName("поиск по списку из id, позитивный сценарий")
    void readAllByIdPositiveTest() {
        List<HistoryDto> actual = historyService.readAllById(List.of(createDefaultHistory()));

        Assertions.assertEquals(777L, actual.get(0).getAccountAuditId());
        Assertions.assertEquals(777L, actual.get(0).getAntiFraudAuditId());
        Assertions.assertEquals(777L, actual.get(0).getAuthorizationAuditId());
        Assertions.assertEquals(777L, actual.get(0).getPublicBankInfoAuditId());
        Assertions.assertEquals(777L, actual.get(0).getProfileAuditId());
        Assertions.assertEquals(777L, actual.get(0).getTransferAuditId());
    }


    @Test
    @DisplayName("поиск по списку из несуществующих id, негативный сценарий")
    void readAllByNonExistIdIdNegativeTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> historyService.readAllById(List.of(1L)));
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void createByValidHistoryPositiveTest() {
        HistoryDto expected = new HistoryDto(1L, 777L, 777L,
                777L, 777L, 777L, 777L);

        HistoryDto actual = historyService.create(expected);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("сохранение несуществующего history, негативный сценарий")
    void createByNullNegativeTest() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                () -> historyService.create(null));
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        Long validId = createDefaultHistory();
        HistoryDto expected = new HistoryDto(validId, 111L, 111L,
                111L, 111L, 111L, 111L);

        HistoryDto actual = historyService.update(validId, expected);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> historyService.update(1L, validHistoryDto));
    }
}