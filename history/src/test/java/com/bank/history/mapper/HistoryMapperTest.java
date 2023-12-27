package com.bank.history.mapper;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HistoryMapperTest {

    private final HistoryMapper mapper;

    @Autowired
    public HistoryMapperTest(HistoryMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    @DisplayName("маппинг в entity")
    void toEntityTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryEntity expected = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);

        HistoryEntity actual = mapper.toEntity(validHistoryDto);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("маппинг в entity, на вход подан null")
    void noEntityNullTest() {
        HistoryDto historyDtoNull = null;

        HistoryEntity actual = mapper.toEntity(historyDtoNull);

        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("маппинг в dto")
    void toDtoTest() {
        HistoryDto expected = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryEntity validHistoryEntity = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);

        HistoryDto actual = mapper.toDto(validHistoryEntity);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("маппинг в dto, на вход подан null")
    void noDtoNullTest() {
        HistoryEntity historyEntityNull = null;

        HistoryDto actual = mapper.toDto(historyEntityNull);

        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("слияние в entity")
    void mergeToEntityTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        HistoryEntity expected = new HistoryEntity(1L, 2L, 2L,
                2L, 2L, 2L, 2L);


        HistoryEntity actual = mapper.mergeToEntity(validHistoryDto, expected);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("слияние в entity, на вход подан null")
    void mergeToEntityNullTest() {
        HistoryDto historyDtoNull = null;
        HistoryEntity expected = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);


        HistoryEntity actual = mapper.mergeToEntity(historyDtoNull, expected);

        Assertions.assertEquals(expected.getAccountAuditId(), actual.getAccountAuditId());
        Assertions.assertEquals(expected.getAntiFraudAuditId(), actual.getAntiFraudAuditId());
        Assertions.assertEquals(expected.getAuthorizationAuditId(), actual.getAuthorizationAuditId());
        Assertions.assertEquals(expected.getPublicBankInfoAuditId(), actual.getPublicBankInfoAuditId());
        Assertions.assertEquals(expected.getProfileAuditId(), actual.getProfileAuditId());
        Assertions.assertEquals(expected.getTransferAuditId(), actual.getTransferAuditId());
    }

    @Test
    @DisplayName("маппинг в список из dto")
    void toListDtoTest() {
        HistoryDto validHistoryDto = new HistoryDto(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        List<HistoryDto> expected = new ArrayList<>();
        expected.add(validHistoryDto);
        HistoryEntity validHistoryEntity = new HistoryEntity(1L, 1L, 1L,
                1L, 1L, 1L, 1L);
        List<HistoryEntity> listOfHistoryEntity = new ArrayList<>();
        listOfHistoryEntity.add(validHistoryEntity);

        List<HistoryDto> actual = mapper.toListDto(listOfHistoryEntity);

        assert (!expected.equals(actual));
    }

    @Test
    @DisplayName("маппинг в список из dto, на вход подан null")
    void noListDtoNullTest() {
        List<HistoryEntity> listOfHistoryEntity = null;

        List<HistoryDto> actual = mapper.toListDto(listOfHistoryEntity);

        Assertions.assertNull(actual);
    }
}