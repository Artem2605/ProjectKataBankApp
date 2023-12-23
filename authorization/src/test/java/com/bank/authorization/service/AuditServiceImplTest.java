package com.bank.authorization.service;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.AuditEntity;
import com.bank.authorization.mapper.AuditMapper;
import com.bank.authorization.repository.AuditRepository;
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
public class AuditServiceImplTest {

    @Mock
    private AuditMapper mapper;
    @Mock
    private AuditRepository repository;
    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        AuditDto auditDto = new AuditDto();
        auditDto.setId(1L);

        Mockito.doReturn(Optional.of(auditEntity)).when(repository).findById(auditEntity.getId());
        Mockito.doReturn(auditDto).when(mapper).toDto(auditEntity);

        AuditDto byId = auditService.findById(auditDto.getId());

        Assertions.assertEquals(byId, auditDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeNest() {
        Mockito.doReturn(Optional.ofNullable(null)).when(repository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            auditService.findById(1L);
        });
    }
}
