package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditMapperTest {
    private final AuditMapper auditMapper;

    @Autowired
    public AuditMapperTest(AuditMapper auditMapper) {
        this.auditMapper = auditMapper;
    }

    @Test
    @DisplayName("Маппинг в Dto")
    void toDtoTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityJson("Test");

        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        auditDto.setEntityJson("Test");

        Assertions.assertEquals(auditDto.getId(), auditMapper.toDto(auditEntity).getId());
        Assertions.assertEquals(auditDto.getEntityJson(), auditMapper.toDto(auditEntity).getEntityJson());
    }

    @Test
    @DisplayName("Маппинг в Dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(auditMapper.toDto(null));
    }
}
