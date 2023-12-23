package com.bank.authorization.controller;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.service.UserService;
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
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");
        Mockito.doReturn(userDto).when(userService).findById(userDto.getId());

        ResponseEntity<UserDto> read = userController.read(userDto.getId());

        Assertions.assertEquals(read.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(read.getBody(), userDto);
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeNest() {
        Long testId = 10L;
        Mockito.doThrow(new EntityNotFoundException()).when(userService).findById(testId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userController.read(testId);
        });
    }

    @Test
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");
        Mockito.doReturn(userDto).when(userService).save(userDto);

        ResponseEntity<UserDto> create = userController.create(userDto);

        Assertions.assertEquals(create.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(create.getBody(), userDto);
    }

    @Test
    @DisplayName("Создание null, негативный сценарий")
    void createNullDtoNegativeTest() {
        UserDto userDto = new UserDto();
        Mockito.doThrow(new NullPointerException()).when(userService).save(userDto);

        Assertions.assertThrows(NullPointerException.class, () -> {
            userController.create(userDto);
        });
    }

    @Test
    @DisplayName("обновление данных user в базе данных, позитивный сценарий")
    void updatePositiveTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");
        Mockito.doReturn(userDto).when(userService).update(userDto.getId(), userDto);

        ResponseEntity<UserDto> update = userController.update(userDto.getId(), userDto);

        Assertions.assertEquals(update.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(update.getBody(), userDto);
    }

    @Test
    @DisplayName("обновление данных user по несуществующему id, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        Long testId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");

        Mockito.doThrow(new EntityNotFoundException()).when(userService).update(testId, userDto);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userController.update(testId, userDto);
        });
    }

    @Test
    @DisplayName("чтение всех users по списку id, позитивный сценарий")
    void readAllPositiveTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);

        List<UserDto> usersDto = List.of(userDto, userDto2);
        List<Long> ids = List.of(userDto.getId(), userDto2.getId());

        Mockito.doReturn(usersDto).when(userService).findAllByIds(ids);

        ResponseEntity<List<UserDto>> listResponseEntity = userController.readAll(ids);

        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(listResponseEntity.getBody(), usersDto);
    }

    @Test
    @DisplayName("чтение всех users по списку, имеющим несуществующий id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        List<Long> ids = List.of(1L, 2L);

        Mockito.doThrow(new EntityNotFoundException()).when(userService).findAllByIds(ids);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userController.readAll(ids);
        });
    }
}
