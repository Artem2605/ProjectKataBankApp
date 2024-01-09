package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository repository;

    @Mock
    private AuditMapper mapper;

    @InjectMocks
    private AuditServiceImpl service;

    private static AuditEntity entity;
    private static AuditDto dto;

    @BeforeAll
    static void prepare() {
        entity = new AuditEntity();
        dto = new AuditDto();
    }

    @Test
    @DisplayName("Поиск аудита по id, позитивный сценарий")
    void findByIdPositive() {
        entity.setId(1L);
        dto.setId(1L);

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        AuditDto foundAuditDto = service.findById(dto.getId());

        assertThat(foundAuditDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск аудита по несуществующему id, выброс исключения")
    void findByIdShouldThrowsEntityNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(repository).findById(47L);

        assertThrows(EntityNotFoundException.class, () -> service.findById(47L));
    }
}