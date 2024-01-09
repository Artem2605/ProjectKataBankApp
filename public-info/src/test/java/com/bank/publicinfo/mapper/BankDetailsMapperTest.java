package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class BankDetailsMapperTest {

    private final BankDetailsMapper mapper = Mappers.getMapper(BankDetailsMapper.class);

    private static BankDetailsEntity entity;
    private static BankDetailsDto dto;

    @BeforeAll
    static void prepare() {
        entity = new BankDetailsEntity();
        dto = new BankDetailsDto();
    }

    @Test
    @DisplayName("Мапинг в entity")
    void toEntityTest() {
        entity.setId(1L);
        entity.setName("Test name");
        entity.setKpp(11L);
        entity.setInn(22L);
        entity.setBik(33L);
        entity.setCity("Test city");
        entity.setCorAccount(new BigDecimal(88));
        entity.setJointStockCompany("Test company");
        dto.setId(1L);
        dto.setName("Test name");
        dto.setKpp(11L);
        dto.setInn(22L);
        dto.setBik(33L);
        dto.setCity("Test city");
        dto.setCorAccount(new BigDecimal(88));
        dto.setJointStockCompany("Test company");

        BankDetailsEntity mappedBankDetailsEntity = mapper.toEntity(dto);
        mappedBankDetailsEntity.setId(1L);

        assertThat(mappedBankDetailsEntity).isEqualTo(entity);
    }

    @Test
    @DisplayName("Мапинг в entity, на вход подан null")
    void toEntityNullTest() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Мапинг в dto")
    void toDtoTest() {
        entity.setId(1L);
        entity.setName("Test name");
        entity.setKpp(11L);
        entity.setInn(22L);
        entity.setBik(33L);
        entity.setCity("Test city");
        entity.setCorAccount(new BigDecimal(88));
        entity.setJointStockCompany("Test company");
        dto.setId(1L);
        dto.setName("Test name");
        dto.setKpp(11L);
        dto.setInn(22L);
        dto.setBik(33L);
        dto.setCity("Test city");
        dto.setCorAccount(new BigDecimal(88));
        dto.setJointStockCompany("Test company");

        BankDetailsDto mappedBankDetailsDto = mapper.toDto(entity);

        assertThat(mappedBankDetailsDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("мапинг в dto, на вход подан null")
    void toDtoNullTest() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Слияние в entity")
    void mergeToEntityTest() {
        entity.setId(1L);
        entity.setName("Test name");
        entity.setKpp(11L);
        entity.setInn(22L);
        entity.setBik(33L);
        entity.setCity("Test city");
        entity.setCorAccount(new BigDecimal(88));
        entity.setJointStockCompany("Test company");
        dto.setId(1L);
        dto.setName("New test name");
        dto.setKpp(99L);
        dto.setInn(88L);
        dto.setBik(45L);
        dto.setCity("New test city");
        dto.setCorAccount(new BigDecimal(99));
        dto.setJointStockCompany("New test company");
        BankDetailsEntity expectedBankDetailsEntity = new BankDetailsEntity();
        expectedBankDetailsEntity.setId(entity.getId());
        expectedBankDetailsEntity.setName(dto.getName());
        expectedBankDetailsEntity.setKpp(dto.getKpp());
        expectedBankDetailsEntity.setInn(dto.getInn());
        expectedBankDetailsEntity.setBik(dto.getBik());
        expectedBankDetailsEntity.setCity(dto.getCity());
        expectedBankDetailsEntity.setCorAccount(dto.getCorAccount());
        expectedBankDetailsEntity.setJointStockCompany(dto.getJointStockCompany());

        BankDetailsEntity mappedBankDetailsEntity = mapper.mergeToEntity(dto,entity);

        assertThat(mappedBankDetailsEntity).isEqualTo(expectedBankDetailsEntity);
    }

    @Test
    @DisplayName("Слияние в entity, на вход подан null")
    void mergeToEntityNullTest() {
        assertNull(mapper.mergeToEntity(null,null));
    }

    @Test
    @DisplayName("Мапинг в dto лист")
    void toDtoListTest() {
        BankDetailsEntity entity1 = new BankDetailsEntity();
        BankDetailsEntity entity2 = new BankDetailsEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<BankDetailsEntity> bankDetailsEntityList = List.of(entity, entity1, entity2);
        BankDetailsDto dto1 = new BankDetailsDto();
        BankDetailsDto dto2 = new BankDetailsDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<BankDetailsDto> bankDetailsDtoList = List.of(dto, dto1, dto2);

        List<BankDetailsDto> mappedBankDetailsDtoList = mapper.toDtoList(bankDetailsEntityList);

        assertThat(mappedBankDetailsDtoList).isEqualTo(bankDetailsDtoList);
    }

    @Test
    @DisplayName("Мапинг в dto лист, на вход подан null")
    void toDtoListNullTest() {
        assertNull(mapper.toDtoList(null));
    }
}