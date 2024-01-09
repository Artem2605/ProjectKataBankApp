package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.LicenseRepository;
import com.bank.publicinfo.service.impl.LicenseServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LicenseServiceImplTest {

    @Mock
    private LicenseRepository repository;

    @Mock
    private LicenseMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplier;

    @InjectMocks
    private LicenseServiceImpl service;

    private static LicenseEntity entity;
    private static LicenseDto dto;

    @BeforeAll
    static void prepare() {
        entity = new LicenseEntity();
        dto = new LicenseDto();
    }

    @Test
    @DisplayName("Поиск всех по листу id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        LicenseEntity entity1 = new LicenseEntity();
        LicenseEntity entity2 = new LicenseEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<LicenseEntity> licenseEntityList = List.of(entity, entity1, entity2);
        LicenseDto dto1 = new LicenseDto();
        LicenseDto dto2 = new LicenseDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<LicenseDto> licenseDtoList = List.of(dto, dto1, dto2);

        Mockito.doReturn(licenseEntityList).when(repository).findAllById(List.of(1L,2L,3L));
        Mockito.doReturn(licenseDtoList).when(mapper).toDtoList(licenseEntityList);

        List<LicenseDto> foundLicenseDto = service.findAllById(List.of(1L,2L,3L));

        assertThat(foundLicenseDto).isEqualTo(licenseDtoList);
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, негативный сценарий")
    void findAllByNonExistIdListNegativeTest () {
        LicenseEntity entity1 = new LicenseEntity();
        LicenseEntity entity2 = new LicenseEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<LicenseEntity> licenseEntityList = List.of(entity,entity1, entity2);

        Mockito.doReturn(licenseEntityList).when(repository).findAllById(List.of(1L,2L,3L,4L,5L));
        Mockito.doThrow(EntityNotFoundException.class)
                .when(supplier)
                .checkForSizeAndLogging(anyString(), eq(List.of(1L,2L,3L,4L,5L)), eq(licenseEntityList));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L,2L,3L,4L,5L)));
    }

    @Test
    @DisplayName("Создание нового объекта, позитивный сценарий")
    void createPositiveTest() {
        dto.setId(48L);
        dto.setBankDetails(new BankDetailsDto());
        dto.setPhotoLicense(new Byte[] {0,1,2,3});
        entity.setId(48L);
        entity.setBankDetails(new BankDetailsEntity());
        entity.setPhotoLicense(new Byte[] {0,1,2,3});

        Mockito.doReturn(entity).when(mapper).toEntity(dto);
        Mockito.doReturn(entity).when(repository).save(entity);
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        LicenseDto newLicenseDto = service.create(dto);

        assertThat(newLicenseDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() {
        dto.setId(365L);
        dto.setBankDetails(new BankDetailsDto());
        dto.setPhotoLicense(new Byte[] {0,1,2,3});
        entity.setId(48L);
        entity.setBankDetails(new BankDetailsEntity());
        entity.setPhotoLicense(new Byte[] {0,1,2,3});
        LicenseEntity expectedMergedEntity = new LicenseEntity();
        expectedMergedEntity.setId(entity.getId());
        expectedMergedEntity.setBankDetails(entity.getBankDetails());
        expectedMergedEntity.setPhotoLicense(dto.getPhotoLicense());
        LicenseDto expectedMergedDto = new LicenseDto();
        expectedMergedDto.setId(entity.getId());
        expectedMergedDto.setBankDetails(dto.getBankDetails());
        expectedMergedDto.setPhotoLicense(dto.getPhotoLicense());

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(expectedMergedEntity).when(mapper).mergeToEntity(dto, entity);
        Mockito.doReturn(expectedMergedDto).when(mapper).toDto(expectedMergedEntity);

        LicenseDto updatedLicenseDto = service.update(entity.getId(), dto);

        assertThat(updatedLicenseDto).isEqualTo(expectedMergedDto);
    }

    @Test
    @DisplayName("Обновление данных по несуществующему id, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.update(any(), dto));
    }

    @Test
    @DisplayName("Поиск одного по id, позитивный сценарий")
    void findByIdPositiveTest() {
        entity.setId(1L);
        dto.setId(1L);

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        LicenseDto foundLicenseDto = service.findById(entity.getId());

        assertThat(foundLicenseDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск одного по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}