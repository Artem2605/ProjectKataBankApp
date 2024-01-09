package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.CertificateEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class CertificateMapperTest {

    private final CertificateMapper mapper = Mappers.getMapper(CertificateMapper.class);

    private static CertificateEntity entity;
    private static CertificateDto dto;
    private static BankDetailsEntity bankDetailsEntity;
    private static BankDetailsDto bankDetailsDto;

    @BeforeAll
    static void prepare() {
        entity = new CertificateEntity();
        dto = new CertificateDto();
        bankDetailsEntity = new BankDetailsEntity(
                1L,
                2L,
                3L,
                4L,
                new BigDecimal(5),
                "Test city",
                "Test company",
                "Test name"
        );
        bankDetailsDto = new BankDetailsDto(
                1L,
                2L,
                3L,
                4L,
                new BigDecimal(5),
                "Test city",
                "Test company",
                "Test name"
        );
    }

    @Test
    @DisplayName("Мапинг в entity")
    void toEntityTest() {
        entity.setId(64L);
        entity.setPhotoCertificate(new Byte[] {0,1,2,3,4,5});
        entity.setBankDetails(bankDetailsEntity);
        dto.setId(64L);
        dto.setPhotoCertificate(new Byte[] {0,1,2,3,4,5});
        dto.setBankDetails(bankDetailsDto);

        CertificateEntity mappedCertificateEntity = mapper.toEntity(dto);
        mappedCertificateEntity.setId(64L);

        assertThat(mappedCertificateEntity).isEqualTo(entity);
    }

    @Test
    @DisplayName("Мапинг в entity, на вход подан null")
    void toEntityNullTest() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Мапинг в dto")
    void toDtoTest() {
        entity.setId(64L);
        entity.setPhotoCertificate(new Byte[] {0,1,2,3,4,5});
        entity.setBankDetails(bankDetailsEntity);
        dto.setId(64L);
        dto.setPhotoCertificate(new Byte[] {0,1,2,3,4,5});
        dto.setBankDetails(bankDetailsDto);

        CertificateDto mappedCertificateDto = mapper.toDto(entity);

        assertThat(mappedCertificateDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Мапинг в dto, на вход подан null")
    void toDtoNullTest() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Слияние в entity")
    void mergeToEntityTest() {
        entity.setId(64L);
        entity.setPhotoCertificate(new Byte[] {0,1,2,3,4,5});
        entity.setBankDetails(bankDetailsEntity);
        dto.setId(99L);
        dto.setPhotoCertificate(new Byte[] {14,15,3,18,55,2});
        dto.setBankDetails(bankDetailsDto);
        CertificateEntity expextedMappedCertificateEntity = new CertificateEntity();
        expextedMappedCertificateEntity.setId(entity.getId());
        expextedMappedCertificateEntity.setPhotoCertificate(dto.getPhotoCertificate());
        expextedMappedCertificateEntity.setBankDetails(entity.getBankDetails());

        CertificateEntity mappedCertificateEntity = mapper.mergeToEntity(dto,entity);

        assertThat(mappedCertificateEntity).isEqualTo(expextedMappedCertificateEntity);
    }

    @Test
    @DisplayName("Слияние в entity, на взод подан null")
    void mergeToEntityNullTest() {
        assertNull(mapper.mergeToEntity(null,null));
    }

    @Test
    @DisplayName("Мапинг в dto лист")
    void toDtoListTest() {
        CertificateEntity entity1 = new CertificateEntity();
        CertificateEntity entity2 = new CertificateEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<CertificateEntity> certificateEntityList = List.of(entity, entity1, entity2);
        CertificateDto dto1 = new CertificateDto();
        CertificateDto dto2 = new CertificateDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<CertificateDto> certificateDtoList = List.of(dto, dto1, dto2);

        List<CertificateDto> mappedCertificateDtoList = mapper.toDtoList(certificateEntityList);

        assertThat(mappedCertificateDtoList).isEqualTo(certificateDtoList);
    }

    @Test
    @DisplayName("Мапинг в dto лист, на вход подан null")
    void toDtoListNullTest() {
        assertNull(mapper.toDtoList(null));
    }
}