package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SuspiciousAccountTransferMapperTest {
    private final SuspiciousAccountTransferMapper suspiciousAccountTransferMapper;

    @Autowired
    public SuspiciousAccountTransferMapperTest(SuspiciousAccountTransferMapper suspiciousAccountTransferMapper) {
        this.suspiciousAccountTransferMapper = suspiciousAccountTransferMapper;
    }

    @Test
    @DisplayName("Маппинг в Dto")
    void toDtoTest() {
        SuspiciousAccountTransferEntity suspAccountTransferEntity = new SuspiciousAccountTransferEntity();
        suspAccountTransferEntity.setId(1L);
        suspAccountTransferEntity.setSuspiciousReason("Test");

        SuspiciousAccountTransferDto suspAccountTransferDto = new SuspiciousAccountTransferDto();
        suspAccountTransferDto.setId(1L);
        suspAccountTransferDto.setSuspiciousReason("Test");
        SuspiciousAccountTransferDto actual = suspiciousAccountTransferMapper.toDto(suspAccountTransferEntity);
        Assertions.assertEquals(suspAccountTransferDto, actual);
    }

    @Test
    @DisplayName("Маппинг в Dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(suspiciousAccountTransferMapper.toDto(null));
    }

    @Test
    @DisplayName("Маппинг в Entity")
    void toEntityTest() {
        SuspiciousAccountTransferDto suspAccountTransferDto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity suspAccountTransferEntity = new SuspiciousAccountTransferEntity(1L,
                2L, false, true, "", "Test");

        Assertions.assertEquals(suspiciousAccountTransferMapper.toEntity(suspAccountTransferDto).getAccountTransferId(),
                suspAccountTransferEntity.getAccountTransferId());
        Assertions.assertEquals(suspiciousAccountTransferMapper.toEntity(suspAccountTransferDto).getIsBlocked(),
                suspAccountTransferEntity.getIsBlocked());
        Assertions.assertEquals(suspiciousAccountTransferMapper.toEntity(suspAccountTransferDto).getIsSuspicious(),
                suspAccountTransferEntity.getIsSuspicious());
        Assertions.assertEquals(suspiciousAccountTransferMapper.toEntity(suspAccountTransferDto).getSuspiciousReason(),
                suspAccountTransferEntity.getSuspiciousReason());
        Assertions.assertEquals(suspiciousAccountTransferMapper.toEntity(suspAccountTransferDto).getBlockedReason(),
                suspAccountTransferEntity.getBlockedReason());
    }

    @Test
    @DisplayName("Маппинг в Entity, на вход подан null")
    void toEntityNullTest() {
        Assertions.assertNull(suspiciousAccountTransferMapper.toEntity(null));
    }

    @Test
    @DisplayName("Маппинг списка Entity в список Dto")
    void toDtoListTest() {
        SuspiciousAccountTransferEntity suspiciousAccountTransferEntityFirst = new SuspiciousAccountTransferEntity();
        suspiciousAccountTransferEntityFirst.setId(1L);
        SuspiciousAccountTransferEntity suspiciousAccountTransferEntitySecond = new SuspiciousAccountTransferEntity();
        suspiciousAccountTransferEntitySecond.setId(2L);
        List<SuspiciousAccountTransferEntity> entityList = List.of(suspiciousAccountTransferEntityFirst, suspiciousAccountTransferEntitySecond);

        SuspiciousAccountTransferDto suspiciousAccountTransferDtoFirst = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDtoFirst.setId(1L);
        SuspiciousAccountTransferDto suspiciousAccountTransferDtoSecond = new SuspiciousAccountTransferDto();
        suspiciousAccountTransferDtoSecond.setId(2L);
        List<SuspiciousAccountTransferDto> dtoList = List.of(suspiciousAccountTransferDtoFirst,
                suspiciousAccountTransferDtoSecond);
        Assertions.assertEquals(dtoList, suspiciousAccountTransferMapper.toListDto(entityList));
    }

    @Test
    @DisplayName("Маппинг списка Entity в Dto, на вход подан null")
    void toDtoListNullTest() {
        Assertions.assertNull(suspiciousAccountTransferMapper.toListDto(null));
    }

    @Test
    @DisplayName("Слияние в Entity")
    void mergeToEntityTest() {
        SuspiciousAccountTransferDto suspAccountTransferDto = new SuspiciousAccountTransferDto(1L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity suspAccountTransferEntity = new SuspiciousAccountTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousAccountTransferEntity expected = new SuspiciousAccountTransferEntity(3L, 2L,
                false, true, "", "Test");
        SuspiciousAccountTransferEntity actual = suspiciousAccountTransferMapper.mergeToEntity(suspAccountTransferDto,
                suspAccountTransferEntity);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Слияние в Entity, на вход подан null")
    void mergeToEntityNullTest() {
        SuspiciousAccountTransferDto suspAccountTransferDto = new SuspiciousAccountTransferDto();
        SuspiciousAccountTransferEntity suspAccountTransferEntity = new SuspiciousAccountTransferEntity(3L,
                4L, false, true, "", "Test");

        SuspiciousAccountTransferEntity expected = new SuspiciousAccountTransferEntity();
        expected.setId(3L);
        SuspiciousAccountTransferEntity actual = suspiciousAccountTransferMapper.mergeToEntity(suspAccountTransferDto,
                suspAccountTransferEntity);
        Assertions.assertEquals(actual, expected);
    }
}