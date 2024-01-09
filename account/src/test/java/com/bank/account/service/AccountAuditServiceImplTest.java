package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AccountAuditMapper;
import com.bank.account.repository.AccountAuditRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountAuditServiceImplTest {
    @Mock
    private AccountAuditMapper accountAuditMapper;
    @Mock
    private AccountAuditRepository accountAuditRepository;
    @InjectMocks
    private AccountAuditServiceImpl accountAuditService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);
        Mockito.doReturn(Optional.of(auditEntity)).when(accountAuditRepository).findById(auditEntity.getId());
        Mockito.doReturn(auditDto).when(accountAuditMapper).toDto(auditEntity);

        Assertions.assertEquals(accountAuditService.findById(auditDto.getId()), auditDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException()).when(accountAuditRepository).findById(1L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountAuditService.findById(1L));
    }
}
