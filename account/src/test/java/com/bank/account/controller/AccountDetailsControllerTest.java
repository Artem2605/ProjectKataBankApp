package com.bank.account.controller;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.service.AccountDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsControllerTest {
    @InjectMocks
    private AccountDetailsController accountDetailsController;

    @Mock
    private AccountDetailsService accountDetailsService;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setId(1L);
        accountDetailsDto.setPassportId(10L);
        Mockito.doReturn(accountDetailsDto).when(accountDetailsService).findById(accountDetailsDto.getId());
        AccountDetailsDto temp = accountDetailsController.read(accountDetailsDto.getId());
        Assertions.assertEquals(temp, accountDetailsDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsService).findById(2L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsController.read(2L));
    }

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setId(1L);
        accountDetailsDto.setPassportId(5L);
        Mockito.doReturn(accountDetailsDto).when(accountDetailsService).save(accountDetailsDto);
        ResponseEntity<AccountDetailsDto> temp = accountDetailsController.create(accountDetailsDto);

        Assertions.assertEquals(temp.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(temp.getBody(), accountDetailsDto);
    }

    @Test
    @DisplayName("Созадние null, негативный сценарий")
    void createNegativeTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        Mockito.doThrow(new NullPointerException()).when(accountDetailsService).save(accountDetailsDto);
        Assertions.assertThrows(NullPointerException.class, () -> accountDetailsController.create(accountDetailsDto));
    }

    @Test
    @DisplayName("Обновление, позитивный сценарий")
    void updatePositiveTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setId(1L);
        accountDetailsDto.setPassportId(5L);
        Mockito.doReturn(accountDetailsDto).when(accountDetailsService)
                .update(accountDetailsDto.getId(), accountDetailsDto);
        ResponseEntity<AccountDetailsDto> temp = accountDetailsController
                .update(accountDetailsDto.getId(), accountDetailsDto);

        Assertions.assertEquals(temp.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(temp.getBody(), accountDetailsDto);
    }

    @Test
    @DisplayName("Обновление, негативный сценарий")
    void updateNegativeTest() {
        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setId(1L);
        accountDetailsDto.setPassportId(5L);
        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsService).update(10L, accountDetailsDto);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsController
                .update(10L, accountDetailsDto));
    }

    @Test
    @DisplayName("Чтение всех сущностей по списку id, позитивный сценарий")
    void readAllPositiveTest() {
        AccountDetailsDto accountDetailsDto1 = new AccountDetailsDto();
        accountDetailsDto1.setId(1L);
        accountDetailsDto1.setPassportId(5L);
        AccountDetailsDto accountDetailsDto2 = new AccountDetailsDto();
        accountDetailsDto2.setId(2L);
        accountDetailsDto2.setPassportId(10L);
        List<AccountDetailsDto> accountDtos = List.of(accountDetailsDto1, accountDetailsDto2);
        List<Long> ids = List.of(accountDetailsDto1.getId(), accountDetailsDto2.getId());

        Mockito.doReturn(accountDtos).when(accountDetailsService).findAllById(ids);
        ResponseEntity<List<AccountDetailsDto>> listTemp = accountDetailsController.readAll(ids);

        Assertions.assertEquals(listTemp.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(listTemp.getBody(), accountDtos);
    }

    @Test
    @DisplayName("Чтение всех сущностей по списку id, негативный сценарий")
    void readAllNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        Mockito.doThrow(new EntityNotFoundException()).when(accountDetailsService).findAllById(ids);
        Assertions.assertThrows(EntityNotFoundException.class, () -> accountDetailsController.readAll(ids));
    }
}
