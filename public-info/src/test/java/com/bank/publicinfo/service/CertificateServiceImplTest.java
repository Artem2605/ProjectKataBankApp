package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.CertificateEntity;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.CertificateRepository;
import com.bank.publicinfo.service.impl.CertificateServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository repository;

    @Mock
    private CertificateMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplier;

    @InjectMocks
    private CertificateServiceImpl service;

    private static CertificateEntity entity;
    private static CertificateDto dto;

    @BeforeAll
    static void prepare() {
        entity = new CertificateEntity();
        dto = new CertificateDto();
    }

    @Test
    @DisplayName("Поиск всех по листу id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        CertificateEntity entity1 = new CertificateEntity();
        CertificateEntity entity2 = new CertificateEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<CertificateEntity> certificateEntityList = List.of(entity, entity1, entity2);
        CertificateDto dto1 = new CertificateDto();
        CertificateDto dto2 = new CertificateDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<CertificateDto> certificateDtoList = List.of(dto, dto1, dto2);

        Mockito.doReturn(certificateEntityList).when(repository).findAllById(List.of(1L,2L,3L));
        Mockito.doReturn(certificateDtoList).when(mapper).toDtoList(certificateEntityList);

        List<CertificateDto> foundCertificateDto = service.findAllById(List.of(1L,2L,3L));

        assertThat(foundCertificateDto).isEqualTo(certificateDtoList);
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, негативный сценарий")
    void findAllByNonExistIdListNegativeTest () {
        CertificateEntity entity1 = new CertificateEntity();
        CertificateEntity entity2 = new CertificateEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<CertificateEntity> certificateEntityList = List.of(entity,entity1, entity2);

        Mockito.doReturn(certificateEntityList).when(repository).findAllById(List.of(1L,2L,3L,4L,5L));
        Mockito.doThrow(EntityNotFoundException.class)
                .when(supplier)
                .checkForSizeAndLogging(anyString(), eq(List.of(1L,2L,3L,4L,5L)), eq(certificateEntityList));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L,2L,3L,4L,5L)));
    }

    @Test
    @DisplayName("Создание нового объекта, позитивный сценарий")
    void createPositiveTest() {
        dto.setId(48L);
        dto.setBankDetails(new BankDetailsDto());
        dto.setPhotoCertificate(new Byte[] {0,1,2,3});
        entity.setId(48L);
        entity.setBankDetails(new BankDetailsEntity());
        entity.setPhotoCertificate(new Byte[] {0,1,2,3});

        Mockito.doReturn(entity).when(mapper).toEntity(dto);
        Mockito.doReturn(entity).when(repository).save(entity);
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        CertificateDto newCertificateDto = service.create(dto);

        assertThat(newCertificateDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() {
        dto.setId(365L);
        dto.setBankDetails(new BankDetailsDto());
        dto.setPhotoCertificate(new Byte[] {0,1,2,3});
        entity.setId(48L);
        entity.setBankDetails(new BankDetailsEntity());
        entity.setPhotoCertificate(new Byte[] {0,1,2,3});
        CertificateEntity expectedMergedEntity = new CertificateEntity();
        expectedMergedEntity.setId(entity.getId());
        expectedMergedEntity.setBankDetails(entity.getBankDetails());
        expectedMergedEntity.setPhotoCertificate(dto.getPhotoCertificate());
        CertificateDto expectedMergedDto = new CertificateDto();
        expectedMergedDto.setId(entity.getId());
        expectedMergedDto.setBankDetails(dto.getBankDetails());
        expectedMergedDto.setPhotoCertificate(dto.getPhotoCertificate());

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(expectedMergedEntity).when(mapper).mergeToEntity(dto, entity);
        Mockito.doReturn(expectedMergedDto).when(mapper).toDto(expectedMergedEntity);

        CertificateDto updatedCertificateDto = service.update(entity.getId(), dto);

        assertThat(updatedCertificateDto).isEqualTo(expectedMergedDto);
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

        CertificateDto foundCertificateDto = service.findById(entity.getId());

        assertThat(foundCertificateDto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск одного по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}