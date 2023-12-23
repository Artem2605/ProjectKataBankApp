package com.bank.authorization.mapper;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.AuditEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuditMapperTest {

    private final AuditMapper auditMapper;

    @Autowired
    public AuditMapperTest(AuditMapper auditMapper) {
        this.auditMapper = auditMapper;
    }

    @Test
    @DisplayName("маппинг в dto")
    void toDtoTest() {

        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityJson("test Json");

        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        auditDto.setEntityJson("test Json");

        Assertions.assertEquals(auditDto, auditMapper.toDto(auditEntity));
    }

    @Test
    @DisplayName("маппинг в dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(auditMapper.toDto(null));
    }
}
