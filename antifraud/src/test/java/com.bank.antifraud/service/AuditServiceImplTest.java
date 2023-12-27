package com.bank.antifraud.service;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import com.bank.antifraud.service.impl.AuditServiceImpl;
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
class AuditServiceImplTest {
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private AuditRepository auditRepository;
    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);

        Mockito.doReturn(Optional.of(auditEntity)).when(auditRepository).findById(auditEntity.getId());
        Mockito.doReturn(auditDto).when(auditMapper).toDto(auditEntity);

        Assertions.assertEquals(auditService.findById(auditDto.getId()), auditDto);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doReturn(Optional.ofNullable(null)).when(auditRepository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            auditService.findById(1L);
        });
    }
}