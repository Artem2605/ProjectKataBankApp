package com.bank.account.service;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
public class AccountDetailsServiceImplTest {
    @Mock
    private AccountDetailsMapper accountDetailsMapper;
    @Mock
    private AccountDetailsRepository accountDetailsRepository;
    @InjectMocks
    private AccountDetailsServiceImpl accountDetailsService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        Mockito.doReturn(Optional.of(accountDetailsEntity)).when(accountDetailsRepository)
                .findById(accountDetailsEntity.getId());
        Mockito.doReturn(accountDetailsDto).when(accountDetailsMapper).toDto(accountDetailsEntity);
        Assertions.assertEquals(accountDetailsService.findById(1L), accountDetailsDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsRepository).findById(10L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsRepository.findById(10L));
    }

    @Test
    @DisplayName("сохранение, позитивный сценарий")
    void savePositiveTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        Mockito.doReturn(accountDetailsEntity).when(accountDetailsMapper).toEntity(accountDetailsDto);
        Mockito.doReturn(accountDetailsEntity).when(accountDetailsRepository).save(accountDetailsEntity);
        Mockito.doReturn(accountDetailsDto).when(accountDetailsMapper).toDto(accountDetailsEntity);

        AccountDetailsDto accountDetailsDtoSaved = accountDetailsService.save(accountDetailsDto);
        Assertions.assertEquals(accountDetailsDtoSaved, accountDetailsDto);
    }

    @Test
    @DisplayName("сохранение, негативный сценарий")
    void saveNegativeTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        Mockito.doReturn(accountDetailsEntity).when(accountDetailsMapper).toEntity(accountDetailsDto);
        Mockito.doReturn(accountDetailsEntity).when(accountDetailsRepository).save(accountDetailsEntity);
        Mockito.doReturn(accountDetailsDto).when(accountDetailsMapper).toDto(accountDetailsEntity);
        Assertions.assertEquals(accountDetailsDto, accountDetailsService.save(accountDetailsDto));
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() {
        AccountDetailsEntity accountDetailsEntity1 = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity2 = new AccountDetailsEntity(1L, 12L, 13L,
                14L, BigDecimal.valueOf(20), false, 15L);
        AccountDetailsDto accountDetailsDto1 = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsDto accountDetailsDto2 = new AccountDetailsDto(1L, 12L, 13L,
                14L, BigDecimal.valueOf(20), false, 15L);

        Mockito.doReturn(Optional.of(accountDetailsEntity1)).when(accountDetailsRepository)
                .findById(accountDetailsDto1.getId());
        Mockito.doReturn(accountDetailsEntity2).when(accountDetailsMapper)
                .mergeToEntity(accountDetailsEntity1, accountDetailsDto1);
        Mockito.doReturn(accountDetailsEntity2).when(accountDetailsRepository).save(accountDetailsEntity2);
        Mockito.doReturn(accountDetailsDto2).when(accountDetailsMapper).toDto(accountDetailsEntity2);

        Assertions.assertEquals(accountDetailsService
                .update(accountDetailsDto1.getId(), accountDetailsDto1), accountDetailsDto2);
    }

    @Test
    @DisplayName("Обновление, негативный сценарий")
    void updateNegativeTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsRepository).findById(2L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsService
                .update(2L, accountDetailsDto));
    }

    @Test
    @DisplayName("поиск всех сущностей по списку id, позитивный сценарий")
    void readAllPositiveTest() {
        AccountDetailsEntity accountDetailsEntity1 = new AccountDetailsEntity(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsEntity accountDetailsEntity2 = new AccountDetailsEntity(2L, 12L, 13L,
                14L, BigDecimal.valueOf(20), false, 15L);
        AccountDetailsDto accountDetailsDto1 = new AccountDetailsDto(1L, 2L, 3L,
                4L, BigDecimal.valueOf(10), true, 5L);
        AccountDetailsDto accountDetailsDto2 = new AccountDetailsDto(2L, 12L, 13L,
                14L, BigDecimal.valueOf(20), false, 15L);
        List<Long> ids = List.of(accountDetailsDto1.getId(), accountDetailsDto2.getId());
        List<AccountDetailsDto> accountDtos = List.of(accountDetailsDto1, accountDetailsDto2);
        List<AccountDetailsEntity> accountDetailsEntities = List.of(accountDetailsEntity1, accountDetailsEntity2);

        Mockito.doReturn(Optional.ofNullable(accountDetailsEntity1)).when(accountDetailsRepository)
                .findById(accountDetailsDto1.getId());
        Mockito.doReturn(Optional.ofNullable(accountDetailsEntity2)).when(accountDetailsRepository)
                .findById(accountDetailsDto2.getId());
        Mockito.doReturn(accountDtos).when(accountDetailsMapper).toDtoList(accountDetailsEntities);

        Assertions.assertEquals(accountDetailsService.findAllById(ids), accountDtos);
    }

    @Test
    @DisplayName("поиск всех сущностей по списку id, негативный сценарий")
    void readAllNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsRepository).findById(1L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsService.findAllById(List.of(1L)));
    }
}
