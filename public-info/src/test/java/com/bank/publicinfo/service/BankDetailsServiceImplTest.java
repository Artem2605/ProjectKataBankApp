package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.service.impl.BankDetailsServiceImpl;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class BankDetailsServiceImplTest {

    @Mock
    private BankDetailsRepository repository;

    @Mock
    private BankDetailsMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplier;

    @InjectMocks
    private BankDetailsServiceImpl service;

    private static BankDetailsEntity entity;
    private static BankDetailsDto dto;

    @BeforeAll
    static void prepare() {
        entity = new BankDetailsEntity();
        dto = new BankDetailsDto();
    }

    @Test
    @DisplayName("Поиск всех по листу id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        BankDetailsEntity entity1 = new BankDetailsEntity();
        BankDetailsEntity entity2 = new BankDetailsEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<BankDetailsEntity> bankDetailsEntityList = List.of(entity,entity1,entity2);
        BankDetailsDto dto1 = new BankDetailsDto();
        BankDetailsDto dto2 = new BankDetailsDto();
        dto.setId(1L);
        dto1.setId(2L);
        dto2.setId(3L);
        List<BankDetailsDto> bankDetailsDtoList = List.of(dto,dto1,dto2);

        Mockito.doReturn(bankDetailsEntityList).when(repository).findAllById(List.of(1L,2L,3L));
        Mockito.doReturn(bankDetailsDtoList).when(mapper).toDtoList(bankDetailsEntityList);

        List<BankDetailsDto> foundBankDetailsDtoList = service.findAllById(List.of(1L,2L,3L));

        assertThat(foundBankDetailsDtoList).isEqualTo(bankDetailsDtoList);
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, негативный сценарий")
    void findAllByNonExistIdListNegativeTest() {
        BankDetailsEntity entity1 = new BankDetailsEntity();
        BankDetailsEntity entity2 = new BankDetailsEntity();
        entity.setId(1L);
        entity1.setId(2L);
        entity2.setId(3L);
        List<BankDetailsEntity> bankDetailsEntityList = List.of(entity,entity1, entity2);

        Mockito.doReturn(bankDetailsEntityList).when(repository).findAllById(List.of(1L,2L,3L,4L,5L));
        Mockito.doThrow(EntityNotFoundException.class)
                .when(supplier)
                .checkForSizeAndLogging(anyString(), eq(List.of(1L,2L,3L,4L,5L)), eq(bankDetailsEntityList));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(List.of(1L,2L,3L,4L,5L)));
    }

    @Test
    @DisplayName("Создание нового объекта, позитивный сценарий")
    void createPositiveTest() {
        dto.setId(2L);
        dto.setBik(58L);
        dto.setCity("Test city");
        dto.setInn(77L);
        dto.setKpp(1869L);
        dto.setCorAccount(new BigDecimal(8));
        dto.setJointStockCompany("Test joint stock company");
        dto.setName("Test name");
        entity.setId(2L);
        entity.setBik(58L);
        entity.setCity("Test city");
        entity.setInn(77L);
        entity.setKpp(1869L);
        entity.setCorAccount(new BigDecimal(8));
        entity.setJointStockCompany("Test joint stock company");
        entity.setName("Test name");

        Mockito.doReturn(entity).when(mapper).toEntity(dto);
        Mockito.doReturn(entity).when(repository).save(entity);
        Mockito.doReturn(dto).when(mapper).toDto(entity);

        BankDetailsDto newBankDetails = service.create(dto);

        assertThat(newBankDetails).isEqualTo(dto);
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() {
        dto.setId(999L);
        dto.setBik(63L);
        dto.setCity("New test city");
        dto.setInn(285L);
        dto.setKpp(164855L);
        dto.setCorAccount(new BigDecimal(6));
        dto.setJointStockCompany("New test joint stock company");
        dto.setName("New test name");
        entity.setId(2L);
        entity.setBik(58L);
        entity.setCity("Test city");
        entity.setInn(77L);
        entity.setKpp(1869L);
        entity.setCorAccount(new BigDecimal(8));
        entity.setJointStockCompany("Test joint stock company");
        entity.setName("Test name");
        BankDetailsEntity expectedMergedEntity = new BankDetailsEntity();
        expectedMergedEntity.setId(entity.getId());
        expectedMergedEntity.setBik(dto.getBik());
        expectedMergedEntity.setCity(dto.getCity());
        expectedMergedEntity.setInn(dto.getInn());
        expectedMergedEntity.setKpp(dto.getKpp());
        expectedMergedEntity.setCorAccount(dto.getCorAccount());
        expectedMergedEntity.setJointStockCompany(dto.getJointStockCompany());
        expectedMergedEntity.setName(dto.getName());
        BankDetailsDto expectedMergedDto = new BankDetailsDto();
        expectedMergedDto.setId(entity.getId());
        expectedMergedDto.setBik(dto.getBik());
        expectedMergedDto.setCity(dto.getCity());
        expectedMergedDto.setInn(dto.getInn());
        expectedMergedDto.setKpp(dto.getKpp());
        expectedMergedDto.setCorAccount(dto.getCorAccount());
        expectedMergedDto.setJointStockCompany(dto.getJointStockCompany());
        expectedMergedDto.setName(dto.getName());

        Mockito.doReturn(Optional.of(entity)).when(repository).findById(entity.getId());
        Mockito.doReturn(expectedMergedEntity).when(mapper).mergeToEntity(dto,entity);
        Mockito.doReturn(expectedMergedDto).when(mapper).toDto(expectedMergedEntity);

        BankDetailsDto updatedBankDetailsDto = service.update(2L, dto);

        assertThat(updatedBankDetailsDto).isEqualTo(expectedMergedDto);
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

        BankDetailsDto foundBankDetails = service.findById(dto.getId());

        assertThat(foundBankDetails).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск одного по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(any());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}