package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountAuditMapperTest {
    private final AccountAuditMapper accountAuditMapper;

    @Autowired
    public AccountAuditMapperTest(AccountAuditMapper accountAuditMapper) {
        this.accountAuditMapper = accountAuditMapper;
    }

    @Test
    @DisplayName("Маппинг в дто")
    void toDtoTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityJson("test");
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        auditDto.setEntityJson("test");
        Assertions.assertEquals(auditDto, accountAuditMapper.toDto(auditEntity));
    }

    @Test
    @DisplayName("маппинг в dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(accountAuditMapper.toDto(null));
    }
}
