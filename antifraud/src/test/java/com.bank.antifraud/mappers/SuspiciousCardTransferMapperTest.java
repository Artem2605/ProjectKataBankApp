package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SuspiciousCardTransferMapperTest {

    private final SuspiciousCardTransferMapper suspiciousCardTransferMapper;

    @Autowired
    public SuspiciousCardTransferMapperTest(SuspiciousCardTransferMapper suspiciousCardTransferMapper) {
        this.suspiciousCardTransferMapper = suspiciousCardTransferMapper;
    }

    @Test
    @DisplayName("Маппинг в Dto")
    void toDtoTest() {
        SuspiciousCardTransferEntity suspiciousCardTransferEntity = new SuspiciousCardTransferEntity();
        suspiciousCardTransferEntity.setId(1L);
        suspiciousCardTransferEntity.setSuspiciousReason("Test");

        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        suspiciousCardTransferDto.setId(1L);
        suspiciousCardTransferDto.setSuspiciousReason("Test");
        SuspiciousCardTransferDto actual = suspiciousCardTransferMapper.toDto(suspiciousCardTransferEntity);
        Assertions.assertEquals(suspiciousCardTransferDto, actual);
    }

    @Test
    @DisplayName("Маппинг в Dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(suspiciousCardTransferMapper.toDto(null));
    }

    @Test
    @DisplayName("Маппинг в Entity")
    void toEntityTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity suspiciousCardTransferEntity = new SuspiciousCardTransferEntity(1L,
                2L, false, true, "", "Test");

        Assertions.assertEquals(suspiciousCardTransferMapper.toEntity(suspiciousCardTransferDto).getCardTransferId(),
                suspiciousCardTransferEntity.getCardTransferId());
        Assertions.assertEquals(suspiciousCardTransferMapper.toEntity(suspiciousCardTransferDto).getIsBlocked(),
                suspiciousCardTransferEntity.getIsBlocked());
        Assertions.assertEquals(suspiciousCardTransferMapper.toEntity(suspiciousCardTransferDto).getIsSuspicious(),
                suspiciousCardTransferEntity.getIsSuspicious());
        Assertions.assertEquals(suspiciousCardTransferMapper.toEntity(suspiciousCardTransferDto).getSuspiciousReason(),
                suspiciousCardTransferEntity.getSuspiciousReason());
        Assertions.assertEquals(suspiciousCardTransferMapper.toEntity(suspiciousCardTransferDto).getBlockedReason(),
                suspiciousCardTransferEntity.getBlockedReason());
    }

    @Test
    @DisplayName("Маппинг в Entity, на вход подан null")
    void toEntityNullTest() {
        Assertions.assertNull(suspiciousCardTransferMapper.toEntity(null));
    }

    @Test
    @DisplayName("Маппинг списка Entity в список Dto")
    void toDtoListTest() {
        SuspiciousCardTransferEntity suspiciousCardTransferEntityFirst = new SuspiciousCardTransferEntity();
        suspiciousCardTransferEntityFirst.setId(1L);
        SuspiciousCardTransferEntity suspiciousCardTransferEntitySecond = new SuspiciousCardTransferEntity();
        suspiciousCardTransferEntitySecond.setId(2L);
        List<SuspiciousCardTransferEntity> entityList = List.of(suspiciousCardTransferEntityFirst,
                suspiciousCardTransferEntitySecond);

        SuspiciousCardTransferDto suspiciousCardTransferDtoFirst = new SuspiciousCardTransferDto();
        suspiciousCardTransferDtoFirst.setId(1L);
        SuspiciousCardTransferDto suspiciousCardTransferDtoSecond = new SuspiciousCardTransferDto();
        suspiciousCardTransferDtoSecond.setId(2L);
        List<SuspiciousCardTransferDto> dtoList = List.of(suspiciousCardTransferDtoFirst,
                suspiciousCardTransferDtoSecond);
        Assertions.assertEquals(dtoList, suspiciousCardTransferMapper.toListDto(entityList));
    }

    @Test
    @DisplayName("Маппинг списка Entity в Dto, на вход подан null")
    void toDtoListNullTest() {
        Assertions.assertNull(suspiciousCardTransferMapper.toListDto(null));
    }

    @Test
    @DisplayName("Слияние в Entity")
    void mergeToEntityTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity suspiciousCardTransferEntity = new SuspiciousCardTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousCardTransferEntity expected = new SuspiciousCardTransferEntity(3L, 2L,
                false, true, "", "Test");
        SuspiciousCardTransferEntity actual = suspiciousCardTransferMapper.mergeToEntity(suspiciousCardTransferDto,
                suspiciousCardTransferEntity);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Слияние в Entity, на вход подан null")
    void mergeToEntityNullTest() {
        SuspiciousCardTransferDto suspiciousCardTransferDto = new SuspiciousCardTransferDto();
        SuspiciousCardTransferEntity suspiciousCardTransferEntity = new SuspiciousCardTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousCardTransferEntity expected = new SuspiciousCardTransferEntity();
        expected.setId(3L);
        SuspiciousCardTransferEntity actual = suspiciousCardTransferMapper.mergeToEntity(suspiciousCardTransferDto,
                suspiciousCardTransferEntity);
        Assertions.assertEquals(actual, expected);
    }
}