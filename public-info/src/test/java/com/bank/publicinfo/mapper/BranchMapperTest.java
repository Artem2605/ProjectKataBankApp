package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BranchMapperTest {

    private final BranchMapper mapper = Mappers.getMapper(BranchMapper.class);

    private static BranchEntity entity;
    private static BranchDto dto;

    @BeforeAll
    static void prepare() {
        entity = new BranchEntity();
        dto = new BranchDto();
    }

    @Test
    @DisplayName("Перевод в сущность, успешный сценарий")
    void toEntityPositive() {
        entity.setId(86L);
        entity.setCity("Test city");
        entity.setAddress("Test address");
        entity.setPhoneNumber(9877L);
        entity.setEndOfWork(LocalTime.of(23,0,0,0));
        entity.setStartOfWork(LocalTime.of(10,0,0,0));
        dto.setId(86L);
        dto.setCity("Test city");
        dto.setAddress("Test address");
        dto.setPhoneNumber(9877L);
        dto.setEndOfWork(LocalTime.of(23,0,0,0));
        dto.setStartOfWork(LocalTime.of(10,0,0,0));

        BranchEntity mappedBranchEntity = mapper.toEntity(dto);
        mappedBranchEntity.setId(86L);

        assertThat(mappedBranchEntity).isEqualTo(entity);
    }

    @Test
    @DisplayName("Перевод в сущность, dto is null")
    void toEntityWithNullDto() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Перевод в дто, успешный сценарий")
    void toDtoPositive() {
        entity.setId(86L);
        entity.setCity("Test city");
        entity.setAddress("Test address");
        entity.setPhoneNumber(9877L);
        entity.setEndOfWork(LocalTime.of(23,0,0,0));
        entity.setStartOfWork(LocalTime.of(10,0,0,0));
        dto.setId(86L);
        dto.setCity("Test city");
        dto.setAddress("Test address");
        dto.setPhoneNumber(9877L);
        dto.setEndOfWork(LocalTime.of(23,0,0,0));
        dto.setStartOfWork(LocalTime.of(10,0,0,0));

        BranchDto mappedBranchDto = mapper.toDto(entity);

        assertThat(mappedBranchDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Перевод в дто, entity is null")
    void toDtoWithNullEntity() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Объединение, успешный сценарий")
    void mergeToEntityPositive() {
        entity.setId(86L);
        entity.setCity("Test city");
        entity.setAddress("Test address");
        entity.setPhoneNumber(9877L);
        entity.setEndOfWork(LocalTime.of(23,0,0,0));
        entity.setStartOfWork(LocalTime.of(10,0,0,0));
        dto.setId(145L);
        dto.setCity("New test city");
        dto.setAddress("New test address");
        dto.setPhoneNumber(98799L);
        dto.setEndOfWork(LocalTime.of(20,0,0,0));
        dto.setStartOfWork(LocalTime.of(12,0,0,0));
        BranchEntity expectedMappedBranchEntity = new BranchEntity();
        expectedMappedBranchEntity.setId(entity.getId());
        expectedMappedBranchEntity.setCity(dto.getCity());
        expectedMappedBranchEntity.setAddress(dto.getAddress());
        expectedMappedBranchEntity.setPhoneNumber(dto.getPhoneNumber());
        expectedMappedBranchEntity.setEndOfWork(dto.getEndOfWork());
        expectedMappedBranchEntity.setStartOfWork(dto.getStartOfWork());

        BranchEntity mappedBranchEntity = mapper.mergeToEntity(dto,entity);

        assertThat(mappedBranchEntity).isEqualTo(expectedMappedBranchEntity);
    }

    @Test
    @DisplayName("Объединение, dto and entity are null")
    void mergeToEntityWithNullDtoAndEntity() {
        assertNull(mapper.mergeToEntity(null,null));
    }

    @Test
    @DisplayName("Перевод в дто лист, успешный сценрий")
    void toDtoListPositive() {
        BranchEntity entity1 = new BranchEntity();
        BranchEntity entity2 = new BranchEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<BranchEntity> branchEntityList = List.of(entity, entity1, entity2);
        BranchDto dto1 = new BranchDto();
        BranchDto dto2 = new BranchDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<BranchDto> branchDtoList = List.of(dto, dto1, dto2);

        List<BranchDto> mappedBranchDtoList = mapper.toDtoList(branchEntityList);

        assertThat(mappedBranchDtoList).isEqualTo(branchDtoList);
    }

    @Test
    @DisplayName("Перевод в дто лист, EntityList is null")
    void toDtoListWithNullEntityList() {
        assertNull(mapper.toDtoList(null));
    }
}