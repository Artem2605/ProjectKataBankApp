package com.bank.authorization.service;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_USER", 2L, "password");
        UserDto IvanDTO = new UserDto(1L, "ROLE_USER", "password", 2L);

        Mockito.doReturn(Optional.of(IvanEntity)).when(repository).findById(IvanEntity.getId());
        Mockito.doReturn(IvanDTO).when(mapper).toDTO(IvanEntity);

        UserDto byId1 = userService.findById(1L);

        Assertions.assertEquals(byId1, IvanDTO);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeNest() {
        Mockito.doReturn(Optional.ofNullable(null)).when(repository).findById(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(1L);
        });
    }

    @Test
    @DisplayName("сохранение user в базу данных, позитивный сценарий")
    void savePositiveTest() {
        UserDto IvanDTO = new UserDto(1L, "ROLE_USER", "password", 2L);
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_USER", 2L, "password");
        UserDto IvanDTOEncoded = new UserDto(1L, "ROLE_USER",
                new BCryptPasswordEncoder().encode("password"), 2L);

        Mockito.doReturn(IvanEntity).when(mapper).toEntity(IvanDTO);
        Mockito.doReturn(IvanEntity).when(repository).save(IvanEntity);
        Mockito.doReturn(IvanDTOEncoded).when(mapper).toDTO(IvanEntity);

        UserDto save = userService.save(IvanDTO);

        Assertions.assertEquals(save, IvanDTOEncoded);
    }

    @Test
    @DisplayName("сохранение null в базу данных, негативный сценарий")
    void saveDtoNullNegativeTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            userService.save(null);
        });
    }

    @Test
    @DisplayName("обновление данных user, позитивный сценарий")
    void updatePositiveTest() {
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_USER", 2L, "password");
        UserEntity IvanEntityUpdated = new UserEntity(1L, "ROLE_ADMIN", 3L, "password123");
        UserDto IvanDTO = new UserDto(1L, "ROLE_ADMIN", "password123", 3L);
        UserDto IvanDTOUpdated = new UserDto(1L, "ROLE_ADMIN", "password123", 3L);

        Mockito.doReturn(Optional.of(IvanEntity)).when(repository).findById(IvanDTO.getId());
        Mockito.doReturn(IvanEntityUpdated).when(mapper).mergeToEntity(IvanDTO, IvanEntity);
        Mockito.doReturn(IvanEntityUpdated).when(repository).save(IvanEntityUpdated);
        Mockito.doReturn(IvanDTOUpdated).when(mapper).toDTO(IvanEntityUpdated);

        UserDto updatedUser = userService.update(IvanDTO.getId(), IvanDTO);

        Assertions.assertEquals(updatedUser, IvanDTOUpdated);
    }

    @Test
    @DisplayName("обновление данных user, которого нет в базе данных, негативный сценарий")
    void updateNonExistIdNegativeTest() {
        UserDto IvanDTO = new UserDto(1L, "ROLE_ADMIN", "password123", 3L);

        Mockito.doReturn(Optional.ofNullable(null)).when(repository).findById(IvanDTO.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.update(1L, IvanDTO);
        });
    }

    @Test
    @DisplayName("поиск всех users по списку id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_ADMIN", 3L, "password123");
        UserEntity PetrEntity = new UserEntity(2L, "ROLE_USER", 4L, "password");
        UserDto IvanDTO = new UserDto(1L, "ROLE_ADMIN", "password123", 3L);
        UserDto PetrDTO = new UserDto(2L, "ROLE_USER", "password", 4L);
        List<Long> ids = List.of(IvanDTO.getId(), PetrDTO.getId());
        List<UserDto> usersDTO = List.of(IvanDTO, PetrDTO);
        List<UserEntity> usersEntity = List.of(IvanEntity, PetrEntity);

        Mockito.doReturn(Optional.ofNullable(IvanEntity)).when(repository).findById(IvanDTO.getId());
        Mockito.doReturn(Optional.ofNullable(PetrEntity)).when(repository).findById(PetrDTO.getId());
        Mockito.doReturn(usersDTO).when(mapper).toDtoList(usersEntity);

        List<UserDto> allByIds = userService.findAllByIds(ids);

        Assertions.assertEquals(allByIds, usersDTO);
    }

    @Test
    @DisplayName("поиск всех users по списку, имеющим несуществующий id, негативный сценарий")
    void findAllByIdsNonExistIdNegativeTest() {
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_ADMIN", 3L, "password123");
        UserEntity PetrEntity = new UserEntity(2L, "ROLE_USER", 4L, "password");
        UserDto IvanDTO = new UserDto(1L, "ROLE_ADMIN", "password123", 3L);
        UserDto PetrDTO = new UserDto(2L, "ROLE_USER", "password", 4L);
        List<Long> ids = List.of(IvanDTO.getId(), PetrDTO.getId(), 4L);

        Mockito.doReturn(Optional.ofNullable(IvanEntity)).when(repository).findById(IvanDTO.getId());
        Mockito.doReturn(Optional.ofNullable(PetrEntity)).when(repository).findById(PetrDTO.getId());
        Mockito.doReturn(Optional.ofNullable(null)).when(repository).findById(4L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.findAllByIds(ids);
        });
    }
}
