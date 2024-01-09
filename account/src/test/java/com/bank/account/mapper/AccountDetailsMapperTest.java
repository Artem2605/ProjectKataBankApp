package com.bank.account.mapper;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class AccountDetailsMapperTest {
    private final AccountDetailsMapper accountDetailsMapper;

    @Autowired
    public AccountDetailsMapperTest(AccountDetailsMapper accountDetailsMapper) {
        this.accountDetailsMapper = accountDetailsMapper;
    }

    @Test
    @DisplayName("Маппинг в dto")
    void toDtoTest() {
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity();
        accountDetailsEntity.setId(1L);
        accountDetailsEntity.setPassportId(5L);
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setId(1L);
        accountDetailsDto.setPassportId(5L);
        Assertions.assertEquals(accountDetailsDto, accountDetailsMapper.toDto(accountDetailsEntity));
    }

    @Test
    @DisplayName("Маппинг в dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(accountDetailsMapper.toDto(null));
    }

    @Test
    @DisplayName("Маппинг в entity")
    void toEntityTest() {
        AccountDetailsDto accountDetailsDto= new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);

        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getPassportId(), accountDetailsEntity.getPassportId());
        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getAccountNumber(), accountDetailsEntity.getAccountNumber());
        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getBankDetailsId(), accountDetailsEntity.getBankDetailsId());
        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getMoney(), accountDetailsEntity.getMoney());
        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getNegativeBalance(), accountDetailsEntity.getNegativeBalance());
        Assertions.assertEquals(accountDetailsMapper.toEntity(accountDetailsDto)
                .getProfileId(), accountDetailsEntity.getProfileId());
    }

    @Test
    @DisplayName("Маппинг в entity, на вход подан null")
    void toEntityNullTest() {
        Assertions.assertNull(accountDetailsMapper.toEntity(null));
    }

    @Test
    @DisplayName("Маппинг списка entity в dto")
    void toDtoListTest() {
        AccountDetailsEntity accountDetailsEntity1 = new AccountDetailsEntity();
        accountDetailsEntity1.setId(1L);
        AccountDetailsEntity accountDetailsEntity2 = new AccountDetailsEntity();
        accountDetailsEntity2.setId(2L);
        AccountDetailsDto accountDetailsDto1 = new AccountDetailsDto();
        accountDetailsDto1.setId(1L);
        AccountDetailsDto accountDetailsDto2 = new AccountDetailsDto();
        accountDetailsDto2.setId(2L);
        List<AccountDetailsEntity> accountDetailsEntities = List.of(accountDetailsEntity1, accountDetailsEntity2);
        List<AccountDetailsDto> accountDetailsDtos = List.of(accountDetailsDto1, accountDetailsDto2);

        Assertions.assertEquals(accountDetailsDtos, accountDetailsMapper.toDtoList(accountDetailsEntities));
    }

    @Test
    @DisplayName("Маппинг списка entity в dto, на вход подан null")
    void toDtoListNullTest() {
        Assertions.assertNull(accountDetailsMapper.toDtoList(null));
    }

    @Test
    @DisplayName("Слияние в entity")
    void mergeToEntityTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(2L, 12L, 13L,
                14L, BigDecimal.valueOf(20), false, 15L);
        AccountDetailsEntity accountDetailsEntityExpected = new AccountDetailsEntity(2L, 2L,
                3L, 4L, BigDecimal.valueOf(10), true, 5L);

        Assertions.assertEquals(accountDetailsMapper.mergeToEntity(accountDetailsEntity, accountDetailsDto),
                accountDetailsEntityExpected);
    }

    @Test
    @DisplayName("Слияние в entity, на вход подан null")
    void mergeToEntityNullTest() {
        AccountDetailsDto accountDetailsDto = null;
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntityMerged = accountDetailsMapper
                .mergeToEntity(accountDetailsEntity, accountDetailsDto);

        Assertions.assertEquals(accountDetailsEntity.getPassportId(), accountDetailsEntityMerged.getPassportId());
        Assertions.assertEquals(accountDetailsEntity.getAccountNumber(), accountDetailsEntityMerged.getAccountNumber());
        Assertions.assertEquals(accountDetailsEntity.getBankDetailsId(), accountDetailsEntityMerged.getBankDetailsId());
        Assertions.assertEquals(accountDetailsEntity.getMoney(), accountDetailsEntityMerged.getMoney());
        Assertions.assertEquals(accountDetailsEntity.getNegativeBalance(),
                accountDetailsEntityMerged.getNegativeBalance());
        Assertions.assertEquals(accountDetailsEntity.getProfileId(), accountDetailsEntityMerged.getProfileId());
    }
}
