package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.service.LicenseService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LicenseControllerTest {

    @Mock
    private LicenseService service;

    @InjectMocks
    private LicenseController controller;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static LicenseDto dto;
    private static BankDetailsDto bankDetailsDto;

    @BeforeAll
    static void prepare() {
        bankDetailsDto = new BankDetailsDto(
                5L,
                78L,
                66L,
                789L,
                new BigDecimal(8),
                "Test city",
                "Test joint stock company",
                "Test name"
        );
        dto = new LicenseDto(
                1L,
                new Byte[]{1,2,3,4,5,5,5,6},
                bankDetailsDto
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

        mockMvc.perform(get("/license/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Чтение одного по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        Mockito.doReturn(null).when(service).findById(9L);

        mockMvc.perform(get("/license/{id}", 9L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Чтение всех по id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        LicenseDto dto1 = new LicenseDto();
        LicenseDto dto2 = new LicenseDto();
        dto1.setId(2L);
        dto2.setId(3L);
        List<LicenseDto> licenseDtoList = List.of(dto,dto1,dto2);

        Mockito.doReturn(licenseDtoList).when(service).findAllById(List.of(1L,2L,3L));

        mockMvc.perform(get("/license/read/all")
                        .param("ids", "1", "2", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2, 3)));
    }

    @Test
    @DisplayName("Чтение всех по некорректному листу id, негативный сценарий")
    void readAllByIdNegativeTest() throws Exception {
        Mockito.doReturn(Collections.emptyList()).when(service).findAllById(List.of(78L,56L,536L));

        mockMvc.perform(get("/license/read/all")
                        .param("ids", "78", "56", "536"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание нового, позитивный сценарий")
    void createPositiveTest() throws Exception {
        String licenseDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(dto).when(service).create(dto);

        mockMvc.perform(post("/license/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    @DisplayName("Создание нового, входной дто is null, негативный сценарий")
    void createWithNullDtoTest() throws Exception {
        String licenseDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).create(dto);

        mockMvc.perform(post("/license/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        LicenseDto dto1 = new LicenseDto(
                6L,
                new Byte[]{9,9,9,9,9,9,9,9},
                bankDetailsDto
        );
        String licenseDto1Json = objectMapper.writeValueAsString(dto1);

        Mockito.doReturn(dto1).when(service).update(1L,dto1);

        mockMvc.perform(put("/license/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDto1Json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto1.getId()));
    }

    @Test
    @DisplayName("Обновление данных о несуществующем объекте, негативный сценарий")
    void updateNegativeTest() throws  Exception {
        String licenseDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).update(91L, dto);

        mockMvc.perform(put("/license/update/{id}", 91L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDtoJson))
                .andExpect(status().isOk());
    }
}