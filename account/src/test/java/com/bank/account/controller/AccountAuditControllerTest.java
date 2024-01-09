package com.bank.account.controller;

import com.bank.account.dto.AuditDto;
import com.bank.account.service.AccountAuditService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AccountAuditControllerTest {

    @InjectMocks
    private AccountAuditController accountAuditController;

    @Mock
    private AccountAuditService accountAuditService;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        auditDto.setEntityJson("test");
        Mockito.doReturn(auditDto).when(accountAuditService).findById(auditDto.getId());
        AuditDto auditDtoTemp = accountAuditController.read(auditDto.getId());
        Assertions.assertEquals(auditDtoTemp, auditDto);
    }

    @Test
    @DisplayName("чтение по id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException()).when(accountAuditService).findById(2L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountAuditController.read(2L));
    }
}
