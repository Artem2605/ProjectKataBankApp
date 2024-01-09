package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuditMapperTest {

    private final AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    private static AuditEntity entity;

    @BeforeAll
    static void prepare() {
        entity = new AuditEntity(
                1L,
                "Test entity type",
                "Test operation type",
                "Test creator",
                "Test modifier",
                new Timestamp(8),
                new Timestamp(10),
                "Test new entity json",
                "Test entity json"
        );
    }

    @Test
    @DisplayName("Перевод в dto, успешный сценарий")
    void toDtoPositive() {
        AuditDto mappedAuditDto = mapper.toDto(entity);

        assertEquals(entity.getId(), mappedAuditDto.getId());
        assertEquals(entity.getEntityType(), mappedAuditDto.getEntityType());
        assertEquals(entity.getOperationType(), mappedAuditDto.getOperationType());
        assertEquals(entity.getCreatedBy(), mappedAuditDto.getCreatedBy());
        assertEquals(entity.getModifiedBy(), mappedAuditDto.getModifiedBy());
        assertEquals(entity.getCreatedAt(), mappedAuditDto.getCreatedAt());
        assertEquals(entity.getModifiedAt(), mappedAuditDto.getModifiedAt());
        assertEquals(entity.getNewEntityJson(), mappedAuditDto.getNewEntityJson());
        assertEquals(entity.getEntityJson(), mappedAuditDto.getEntityJson());
    }

    @Test
    @DisplayName("Перевод в dto, entity is null")
    void toDtoWithNullEntity() {
        assertNull(mapper.toDto(null));
    }
}