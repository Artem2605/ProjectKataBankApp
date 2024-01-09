package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.service.impl.AtmServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AtmServiceImplTest {

    @Mock
    private AtmRepository repository;

    @Mock
    private AtmMapper mapper;

    @Mock
    EntityNotFoundSupplier supplier;

    @InjectMocks
    private AtmServiceImpl service;

    private static AtmEntity entity;
    private static AtmDto dto;

    @BeforeAll
    static void prepare() {
        entity = new AtmEntity();
        dto = new AtmDto();
    }

    @Test
    @DisplayName("Поиск всех по листу id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        AtmEntity entity1 = new AtmEntity();
        AtmEntity entity2 = new AtmEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<AtmEntity> atmEntityList = List.of(entity,entity1, entity2);
        AtmDto dto1 = new AtmDto();
        AtmDto dto2 = new AtmDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<AtmDto> atmDtoList = List.of(dto,dto1,dto2);

        doReturn(atmEntityList).when(repository).findAllById(List.of(1L,2L,3L));
        doReturn(atmDtoList).when(mapper).toDtoList(atmEntityList);

        List<AtmDto> foundAtmDtoList = service.findAllById(List.of(1L,2L,3L));

        assertThat(foundAtmDtoList).isEqualTo(atmDtoList);
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, негативный сценарий")
    void findAllByNonExistIdListNegativeTest () {
        AtmEntity entity1 = new AtmEntity();
        AtmEntity entity2 = new AtmEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<AtmEntity> atmEntityList = List.of(entity,entity1, entity2);

        doReturn(atmEntityList).when(repository).findAllById(List.of(1L,2L,3L,4L,5L));
        doThrow(EntityNotFoundException.class)
                .when(supplier)
                .checkForSizeAndLogging(anyString(), eq(List.of(1L,2L,3L,4L,5L)), eq(atmEntityList));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L,2L,3L,4L,5L)));
    }

    @Test
    @DisplayName("Создание нового объекта, позитивный сценарий")
    void createPositiveTest() {
        dto.setId(2L);
        dto.setAddress("Test address");
        dto.setBranch(new BranchDto());
        dto.setAllHours(true);
        dto.setEndOfWork(null);
        dto.setStartOfWork(null);
        entity.setId(2L);
        entity.setAddress("Test address");
        entity.setBranch(new BranchEntity());
        entity.setAllHours(true);
        entity.setEndOfWork(null);
        entity.setStartOfWork(null);

        doReturn(entity).when(mapper).toEntity(dto);
        doReturn(entity).when(repository).save(entity);
        doReturn(dto).when(mapper).toDto(entity);

        AtmDto newAtm = service.create(dto);

        assertThat(newAtm).isEqualTo(dto);
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() {
        dto.setId(45L);
        dto.setAddress("New test address");
        dto.setBranch(new BranchDto());
        dto.setAllHours(true);
        dto.setEndOfWork(null);
        dto.setStartOfWork(null);
        entity.setId(2L);
        entity.setAddress("Test address");
        entity.setBranch(new BranchEntity());
        entity.setAllHours(false);
        entity.setEndOfWork(null);
        entity.setStartOfWork(null);
        AtmEntity expectedMergedEntity = new AtmEntity();
        expectedMergedEntity.setId(entity.getId());
        expectedMergedEntity.setAddress(dto.getAddress());
        expectedMergedEntity.setBranch(entity.getBranch());
        expectedMergedEntity.setAllHours(dto.getAllHours());
        expectedMergedEntity.setStartOfWork(dto.getStartOfWork());
        expectedMergedEntity.setEndOfWork(dto.getEndOfWork());
        AtmDto expectedMergedDto = new AtmDto();
        expectedMergedDto.setId(entity.getId());
        expectedMergedDto.setAddress(dto.getAddress());
        expectedMergedDto.setBranch(dto.getBranch());
        expectedMergedDto.setAllHours(dto.getAllHours());
        expectedMergedDto.setStartOfWork(dto.getStartOfWork());
        expectedMergedDto.setEndOfWork(dto.getEndOfWork());

        doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        doReturn(expectedMergedEntity).when(mapper).mergeToEntity(dto, entity);
        doReturn(expectedMergedDto).when(mapper).toDto(expectedMergedEntity);

        AtmDto updatedAtmDto = service.update(entity.getId(), dto);

        assertThat(updatedAtmDto).isEqualTo(mapper.toDto(expectedMergedEntity));
    }

    @Test
    @DisplayName("Обновление данных по несуществующему id, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.update(any(), dto));
    }

    @Test
    @DisplayName("Поиск одного по id, позитивный сценарий")
    void findByIdPositiveTest() {
        entity.setId(1L);
        dto.setId(1L);

        doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        doReturn(dto).when(mapper).toDto(entity);

        AtmDto foundAtmDto = service.findById(entity.getId());

        assertThat(foundAtmDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск одного по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}