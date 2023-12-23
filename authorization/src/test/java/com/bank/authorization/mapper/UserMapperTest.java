package com.bank.authorization.mapper;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserMapperTest {

    private final UserMapper userMapper;

    @Autowired
    public UserMapperTest(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    @DisplayName("маппинг в dto")
    void toDtoTest() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("password");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("password");

        Assertions.assertEquals(userDto, userMapper.toDTO(userEntity));
    }

    @Test
    @DisplayName("маппинг в dto, на вход подан null")
    void toDtoNullTest() {
        Assertions.assertNull(userMapper.toDTO(null));
    }

    @Test
    @DisplayName("маппинг в entity")
    void toEntityTest() {
        UserDto IvanDTO = new UserDto(1L, "ROLE_USER", "password", 2L);
        UserEntity IvanEntity = new UserEntity(1L, "ROLE_USER", 2L, "password");

        Assertions.assertEquals(userMapper.toEntity(IvanDTO).getPassword(), IvanEntity.getPassword());
        Assertions.assertEquals(userMapper.toEntity(IvanDTO).getProfileId(), IvanEntity.getProfileId());
        Assertions.assertEquals(userMapper.toEntity(IvanDTO).getRole(), IvanEntity.getRole());
    }

    @Test
    @DisplayName("маппинг в entity, на вход подан null")
    void toEntityNullTest() {
        Assertions.assertNull(userMapper.toEntity(null));
    }

    @Test
    @DisplayName("маппинг списка entity в список dto")
    void toDtoListTest() {

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2L);
        List<UserEntity> listEntity = List.of(userEntity1, userEntity2);

        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        userDto1.setId(1L);
        userDto2.setId(2L);
        List<UserDto> listDTO = List.of(userDto1, userDto2);

        Assertions.assertEquals(listDTO, userMapper.toDtoList(listEntity));
    }

    @Test
    @DisplayName("маппинг списка entity в список dto, на вход передан null")
    void toDtoListNullTest() {
        Assertions.assertNull(userMapper.toDtoList(null));
    }

    @Test
    @DisplayName("слияние в entity")
    void mergeToEntityTest() {
        UserDto IvanDTO = new UserDto(1L, "ROLE_USER", "password", 2L);
        UserEntity IvanEntity = new UserEntity(3L, "ROLE_ADMIN", 4L, "password123");

        UserEntity expectedResult = new UserEntity(3L, "ROLE_USER", 2L, "password");

        Assertions.assertEquals(userMapper.mergeToEntity(IvanDTO, IvanEntity), expectedResult);
    }

    @Test
    @DisplayName("слияние в entity, на вход подан null")
    void mergeToEntityNullTest() {
        UserDto IvanDTO = new UserDto();
        UserEntity IvanEntity = new UserEntity(3L, "ROLE_ADMIN", 4L, "password123");

        UserEntity expectedResult = new UserEntity();
        expectedResult.setId(3L);

        Assertions.assertEquals(userMapper.mergeToEntity(IvanDTO, IvanEntity), expectedResult);
    }
}
