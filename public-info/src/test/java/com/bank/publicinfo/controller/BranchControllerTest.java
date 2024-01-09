package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BranchControllerTest {

    @Mock
    private BranchService service;

    @InjectMocks
    private BranchController controller;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static BranchDto dto;

    @BeforeAll
    static void prepare() {
        dto = new BranchDto(
                1L,
                "Test address",
                66L,
                "Test city",
                LocalTime.of(9,0,0,0),
                LocalTime.of(21,0,0,0)
        );
    }

    @BeforeEach
    void eachPrepare() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Чтение одного по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Mockito.doReturn(dto).when(service).findById(1L);

        mockMvc.perform(get("/branch/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Чтение одного по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        Mockito.doReturn(null).when(service).findById(9845L);

        mockMvc.perform(get("/branch/{id}", 9845L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Чтение всех по id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        BranchDto dto1 = new BranchDto();
        BranchDto dto2 = new BranchDto();
        dto1.setId(2L);
        dto2.setId(3L);
        List<BranchDto> branchDtoList = List.of(dto,dto1,dto2);

        Mockito.doReturn(branchDtoList).when(service).findAllById(List.of(1L,2L,3L));

        mockMvc.perform(get("/branch/read/all")
                        .param("ids", "1", "2", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2, 3)));
    }

    @Test
    @DisplayName("Поиск всех по некорректному листу id, негативный сценарий")
    void readAllByIdNegativeTest() throws Exception {
        Mockito.doReturn(Collections.emptyList()).when(service).findAllById(List.of(4L,5L,6L));

        mockMvc.perform(get("/branch/read/all")
                        .param("ids", "4", "5", "6"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание нового, позитивный сценарий")
    void createPositiveTest() throws Exception {
        String branchDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(dto).when(service).create(dto);

        mockMvc.perform(post("/branch/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    @DisplayName("Создание нового, входной дто is null, негативный сценарий")
    void createWithNullDtoTest() throws Exception {
        String branchDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).create(dto);

        mockMvc.perform(post("/branch/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        BranchDto dto1 = new BranchDto();
        dto1.setId(2L);
        String branchDto1Json = objectMapper.writeValueAsString(dto1);

        Mockito.doReturn(dto1).when(service).update(1L,dto1);

        mockMvc.perform(put("/branch/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDto1Json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto1.getId()));
    }

    @Test
    @DisplayName("Обновление данных о несуществующем объекте, негативный сценарий")
    void updateNegativeTest() throws  Exception {
        String branchDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).update(1L, dto);

        mockMvc.perform(put("/branch/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDtoJson))
                .andExpect(status().isOk());
    }
}