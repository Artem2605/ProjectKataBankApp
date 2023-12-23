package com.bank.authorization.controller;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.service.AuditService;
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
public class AuditControllerTest {

    @InjectMocks
    private AuditController auditController;

    @Mock
    private AuditService auditService;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        auditDto.setEntityJson("test value");
        Mockito.doReturn(auditDto).when(auditService).findById(auditDto.getId());

        AuditDto read = auditController.read(auditDto.getId());

        Assertions.assertEquals(read, auditDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeNest() {
        Long testId = 10L;
        Mockito.doThrow(new EntityNotFoundException()).when(auditService).findById(testId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            auditController.read(10L);
        });
    }
}
