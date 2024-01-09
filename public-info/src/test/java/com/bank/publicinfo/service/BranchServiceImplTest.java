package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.BranchRepository;
import com.bank.publicinfo.service.impl.BranchServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private BranchRepository repository;

    @Mock
    private BranchMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplier;

    @InjectMocks
    private BranchServiceImpl service;

    private static BranchEntity entity;
    private static BranchDto dto;

    @BeforeAll
    static void prepare() {
        entity = new BranchEntity();
        dto = new BranchDto();
    }

    @Test
    @DisplayName("Поиск всех по id, успешный сценарий")
    void findAllById() {
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

        Mockito.doReturn(branchEntityList).when(repository).findAllById(List.of(1L,2L,3L));
        Mockito.doReturn(branchDtoList).when(mapper).toDtoList(branchEntityList);

        List<BranchDto> foundBranchDto = service.findAllById(List.of(1L,2L,3L));

        assertThat(foundBranchDto).isEqualTo(branchDtoList);
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, выброс исключения")
    void findAllByIdShouldThrowsEntityNotFoundException () {
        BranchEntity entity1 = new BranchEntity();
        BranchEntity entity2 = new BranchEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<BranchEntity> branchEntityList = List.of(entity, entity1, entity2);

        Mockito.doReturn(branchEntityList).when(repository).findAllById(List.of(1L, 2L, 3L, 4L, 5L));
        Mockito.doThrow(EntityNotFoundException.class)
                .when(supplier)
                .checkForSizeAndLogging(anyString(), eq(List.of(1L, 2L, 3L, 4L, 5L)), eq(branchEntityList));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L, 2L, 3L, 4L, 5L)));
    }

    @Test
    @DisplayName("Добавление нового, успешный сценарий")
    void createPositive() {
        dto.setId(9L);
        dto.setAddress("Test address");
        dto.setStartOfWork(LocalTime.of(10,0,0,0));
        dto.setEndOfWork(LocalTime.of(22,0,0,0));
        dto.setCity("Test city");
        dto.setPhoneNumber(777L);
        entity.setId(9L);
        entity.setAddress("Test address");
        entity.setStartOfWork(LocalTime.of(10,0,0,0));
        entity.setEndOfWork(LocalTime.of(22,0,0,0));
        entity.setCity("Test city");
        entity.setPhoneNumber(777L);

        Mockito.doReturn(entity).when(mapper).toEntity(dto);
        Mockito.doReturn(entity).when(repository).save(entity);
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        BranchDto newBranch = service.create(dto);

        assertThat(newBranch).isEqualTo(dto);
    }

    @Test
    @DisplayName("Обновление данных, успешный сценарий")
    void updatePositive() {
        dto.setId(69L);
        dto.setAddress("New test address");
        dto.setStartOfWork(LocalTime.of(9,0,0,0));
        dto.setEndOfWork(LocalTime.of(21,30,0,0));
        dto.setCity("New test city");
        dto.setPhoneNumber(333L);
        entity.setId(9L);
        entity.setAddress("Test address");
        entity.setStartOfWork(LocalTime.of(10,0,0,0));
        entity.setEndOfWork(LocalTime.of(22,0,0,0));
        entity.setCity("Test city");
        entity.setPhoneNumber(777L);
        BranchEntity expectedMergedEntity = new BranchEntity();
        expectedMergedEntity.setId(entity.getId());
        expectedMergedEntity.setAddress(dto.getAddress());
        expectedMergedEntity.setStartOfWork(dto.getStartOfWork());
        expectedMergedEntity.setEndOfWork(dto.getEndOfWork());
        expectedMergedEntity.setCity(dto.getCity());
        expectedMergedEntity.setPhoneNumber(dto.getPhoneNumber());
        BranchDto expectedMergedDto = new BranchDto();
        expectedMergedDto.setId(entity.getId());
        expectedMergedDto.setAddress(dto.getAddress());
        expectedMergedDto.setStartOfWork(dto.getStartOfWork());
        expectedMergedDto.setEndOfWork(dto.getEndOfWork());
        expectedMergedDto.setCity(dto.getCity());
        expectedMergedDto.setPhoneNumber(dto.getPhoneNumber());

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(expectedMergedEntity).when(mapper).mergeToEntity(dto, entity);
        Mockito.doReturn(expectedMergedDto).when(mapper).toDto(expectedMergedEntity);

        BranchDto updatedBranchDto = service.update(entity.getId(), dto);

        assertThat(updatedBranchDto).isEqualTo(expectedMergedDto);
    }

    @Test
    @DisplayName("Обновление данных по несуществующему id, выброс исключения")
    void updateShouldThrowsEntityNotFoundException() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.update(any(), dto));
    }

    @Test
    @DisplayName("Поиск одного по id, успешный сценарий")
    void findByIdPositive() {
        entity.setId(1L);
        dto.setId(1L);

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        BranchDto foundBranchDto = service.findById(entity.getId());

        assertThat(foundBranchDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск одного по несуществующему id, выброс исключения")
    void findByIdShouldThrowsEntityNotFoundException() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}