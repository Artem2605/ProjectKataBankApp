package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AtmMapperTest {

    private final AtmMapper mapper = Mappers.getMapper(AtmMapper.class);

    private static AtmEntity entity;
    private static AtmDto dto;
    private static BranchEntity branchEntity;
    private static  BranchDto branchDto;

    @BeforeAll
    static void prepare() {
        entity = new AtmEntity();
        dto = new AtmDto();
        branchDto = new BranchDto(
                75L,
                "Test address",
                69L,
                "Test city",
                LocalTime.of(9,0,0,0),
                LocalTime.of(22,30,0,0)
        );
        branchEntity = new BranchEntity(
                75L,
                "Test address",
                69L,
                "Test city",
                LocalTime.of(9,0,0,0),
                LocalTime.of(22,30,0,0)
        );
    }

    @Test
    @DisplayName("Перевод в сущность, успешный сценарий")
    void toEntityPositive() {
        entity.setId(1L);
        entity.setAddress("Test address");
        entity.setBranch(branchEntity);
        entity.setAllHours(false);
        entity.setStartOfWork(LocalTime.of(9,0,0,0));
        entity.setEndOfWork(LocalTime.of(23,0,0,0));
        dto.setId(1L);
        dto.setAddress("Test address");
        dto.setBranch(branchDto);
        dto.setAllHours(false);
        dto.setStartOfWork(LocalTime.of(9,0,0,0));
        dto.setEndOfWork(LocalTime.of(23,0,0,0));

        AtmEntity mappedAtmEntity = mapper.toEntity(dto);
        mappedAtmEntity.setId(1L);

        assertThat(mappedAtmEntity).isEqualTo(entity);
    }

    @Test
    @DisplayName("Перевод в сущность, dto is null")
    void toEntityWithNullDto() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Перевод в дто, успешный сценарий")
    void toDtoPositive() {
        entity.setId(1L);
        entity.setAddress("Test address");
        entity.setBranch(branchEntity);
        entity.setAllHours(false);
        entity.setStartOfWork(LocalTime.of(9,0,0,0));
        entity.setEndOfWork(LocalTime.of(23,0,0,0));
        dto.setId(1L);
        dto.setAddress("Test address");
        dto.setBranch(branchDto);
        dto.setAllHours(false);
        dto.setStartOfWork(LocalTime.of(9,0,0,0));
        dto.setEndOfWork(LocalTime.of(23,0,0,0));

        AtmDto mappedAtmDto = mapper.toDto(entity);

        assertThat(mappedAtmDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Перевод в дто, entity is null")
    void toDtoWithNullEntity() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Объединение, успешный сценарий")
    void mergeToEntityPositive() {
        dto.setId(1L);
        dto.setAddress("New test address");
        dto.setBranch(branchDto);
        dto.setStartOfWork(LocalTime.of(10,0,0,0));
        dto.setEndOfWork(LocalTime.of(23,0,0,0));
        dto.setAllHours(false);
        entity.setId(1L);
        entity.setAddress("Test address");
        entity.setBranch(branchEntity);
        entity.setStartOfWork(LocalTime.of(9,0,0,0));
        entity.setEndOfWork(LocalTime.of(22,30,0,0));
        entity.setAllHours(false);
        AtmEntity expectedMappedEntity = new AtmEntity();
        expectedMappedEntity.setId(dto.getId());
        expectedMappedEntity.setAddress(dto.getAddress());
        expectedMappedEntity.setBranch(branchEntity);
        expectedMappedEntity.setStartOfWork(dto.getStartOfWork());
        expectedMappedEntity.setEndOfWork(dto.getEndOfWork());
        expectedMappedEntity.setAllHours(dto.getAllHours());

        AtmEntity mappedEntity = mapper.mergeToEntity(dto,entity);

        assertThat(mappedEntity).isEqualTo(expectedMappedEntity);
    }

    @Test
    @DisplayName("Объединение, dto and entity are null")
    void mergeToEntityWithNullDtoAndEntity() {
        assertNull(mapper.mergeToEntity(null,null));
    }

    @Test
    @DisplayName("Перевод в дто лист, успешный сценрий")
    void toDtoListPositive() {
        AtmEntity entity1 = new AtmEntity();
        AtmEntity entity2 = new AtmEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<AtmEntity> atmEntityList = List.of(entity, entity1, entity2);
        AtmDto dto1 = new AtmDto();
        AtmDto dto2 = new AtmDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<AtmDto> atmDtoList = List.of(dto, dto1, dto2);

        List<AtmDto> mappedAtmDtoList = mapper.toDtoList(atmEntityList);

        assertThat(mappedAtmDtoList).isEqualTo(atmDtoList);
    }

    @Test
    @DisplayName("Перевод в дто лист, EntityList is null")
    void toDtoListWithNullEntityList() {
        assertNull(mapper.toDtoList(null));
    }
}