package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SuspiciousPhoneTransferMapperTest {

    private final SuspiciousPhoneTransferMapper suspiciousPhoneTransferMapper;

    @Autowired
    public SuspiciousPhoneTransferMapperTest(SuspiciousPhoneTransferMapper suspiciousPhoneTransferMapper) {
        this.suspiciousPhoneTransferMapper = suspiciousPhoneTransferMapper;
    }

    @Test
    @DisplayName("Маппинг в Dto")
    void toDtoTest() {
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntity = new SuspiciousPhoneTransferEntity();
        suspiciousPhoneTransferEntity.setId(1L);
        suspiciousPhoneTransferEntity.setSuspiciousReason("Test");

        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDto.setId(1L);
        suspiciousPhoneTransferDto.setSuspiciousReason("Test");

        Assertions.assertEquals(suspiciousPhoneTransferDto,
                suspiciousPhoneTransferMapper.toDto(suspiciousPhoneTransferEntity));
    }

    @Test
    @DisplayName("Маппинг в Dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(suspiciousPhoneTransferMapper.toDto(null));
    }

    @Test
    @DisplayName("Маппинг в Entity")
    void toEntityTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntity = new SuspiciousPhoneTransferEntity(1L,
                2L, false, true, "", "Test");

        Assertions.assertEquals(suspiciousPhoneTransferMapper.toEntity(suspiciousPhoneTransferDto).getPhoneTransferId(),
                suspiciousPhoneTransferEntity.getPhoneTransferId());
        Assertions.assertEquals(suspiciousPhoneTransferMapper.toEntity(suspiciousPhoneTransferDto).getIsBlocked(),
                suspiciousPhoneTransferEntity.getIsBlocked());
        Assertions.assertEquals(suspiciousPhoneTransferMapper.toEntity(suspiciousPhoneTransferDto).getIsSuspicious(),
                suspiciousPhoneTransferEntity.getIsSuspicious());
        Assertions.assertEquals(suspiciousPhoneTransferMapper.toEntity(suspiciousPhoneTransferDto).getSuspiciousReason(),
                suspiciousPhoneTransferEntity.getSuspiciousReason());
        Assertions.assertEquals(suspiciousPhoneTransferMapper.toEntity(suspiciousPhoneTransferDto).getBlockedReason(),
                suspiciousPhoneTransferEntity.getBlockedReason());
    }

    @Test
    @DisplayName("Маппинг в Entity, на вход подан null")
    void toEntityNullTest() {
        Assertions.assertNull(suspiciousPhoneTransferMapper.toEntity(null));
    }

    @Test
    @DisplayName("Маппинг списка Entity в список Dto")
    void toDtoListTest() {
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntityFirst = new SuspiciousPhoneTransferEntity();
        suspiciousPhoneTransferEntityFirst.setId(1L);
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntitySecond = new SuspiciousPhoneTransferEntity();
        suspiciousPhoneTransferEntitySecond.setId(2L);
        List<SuspiciousPhoneTransferEntity> entityList = List.of(suspiciousPhoneTransferEntityFirst,
                suspiciousPhoneTransferEntitySecond);

        SuspiciousPhoneTransferDto suspiciousPhoneTransferDtoFirst = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDtoFirst.setId(1L);
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDtoSecond = new SuspiciousPhoneTransferDto();
        suspiciousPhoneTransferDtoSecond.setId(2L);
        List<SuspiciousPhoneTransferDto> dtoList = List.of(suspiciousPhoneTransferDtoFirst,
                suspiciousPhoneTransferDtoSecond);

        Assertions.assertEquals(dtoList, suspiciousPhoneTransferMapper.toListDto(entityList));
    }

    @Test
    @DisplayName("Маппинг списка Entity в Dto, на вход подан null")
    void toDtoListNullTest() {
        Assertions.assertNull(suspiciousPhoneTransferMapper.toListDto(null));
    }

    @Test
    @DisplayName("Слияние в Entity")
    void mergeToEntityTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntity = new SuspiciousPhoneTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousPhoneTransferEntity expected = new SuspiciousPhoneTransferEntity(3L, 2L,
                false, true, "", "Test");

        SuspiciousPhoneTransferEntity actual = suspiciousPhoneTransferMapper.mergeToEntity(suspiciousPhoneTransferDto,
                suspiciousPhoneTransferEntity);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Слияние в Entity, на вход подан null")
    void mergeToEntityNullTest() {
        SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = new SuspiciousPhoneTransferDto();
        SuspiciousPhoneTransferEntity suspiciousPhoneTransferEntity = new SuspiciousPhoneTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousPhoneTransferEntity expected = new SuspiciousPhoneTransferEntity();
        expected.setId(3L);
        SuspiciousPhoneTransferEntity actual = suspiciousPhoneTransferMapper.mergeToEntity(suspiciousPhoneTransferDto,
                suspiciousPhoneTransferEntity);

        Assertions.assertEquals(actual, expected);
    }
}