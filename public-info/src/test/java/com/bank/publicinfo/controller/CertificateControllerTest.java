package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.service.CertificateService;
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
class CertificateControllerTest {

    @Mock
    private CertificateService service;

    @InjectMocks
    private CertificateController controller;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static CertificateDto dto;
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
        dto = new CertificateDto(
                1L,
                new Byte[]{1,2,3,4,5},
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

        mockMvc.perform(get("/certificate/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Чтение одного по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        Mockito.doReturn(null).when(service).findById(9L);

        mockMvc.perform(get("/certificate/{id}", 9L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Чтение всех по id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        CertificateDto dto1 = new CertificateDto();
        CertificateDto dto2 = new CertificateDto();
        dto1.setId(2L);
        dto2.setId(3L);
        List<CertificateDto> certificateDtoList = List.of(dto,dto1,dto2);

        Mockito.doReturn(certificateDtoList).when(service).findAllById(List.of(1L,2L,3L));

        mockMvc.perform(get("/certificate/read/all")
                        .param("ids", "1", "2", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2, 3)));
    }

    @Test
    @DisplayName("Чтение всех по некорректному листу id, негативный сценарий")
    void readAllByIdNegativeTest() throws Exception {
        Mockito.doReturn(Collections.emptyList()).when(service).findAllById(List.of(78L,56L,536L));

        mockMvc.perform(get("/certificate/read/all")
                        .param("ids", "78", "56", "536"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание нового, позитивный сценарий")
    void createPositiveTest() throws Exception {
        String certificateDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(dto).when(service).create(dto);

        mockMvc.perform(post("/certificate/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    @DisplayName("Создание нового, входной дто is null, негативный сценарий")
    void createWithNullDtoTest() throws Exception {
        String certificateDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).create(dto);

        mockMvc.perform(post("/certificate/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        CertificateDto dto1 = new CertificateDto(
                2L,
                new Byte[]{9,5,7,6,8,1,1,4},
                bankDetailsDto
        );
        String certificateDto1Json = objectMapper.writeValueAsString(dto1);

        Mockito.doReturn(dto1).when(service).update(1L,dto1);

        mockMvc.perform(put("/certificate/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDto1Json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto1.getId()));
    }

    @Test
    @DisplayName("Обновление данных о несуществующем объекте, негативный сценарий")
    void updateNegativeTest() throws  Exception {
        String certificateDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).update(91L, dto);

        mockMvc.perform(put("/certificate/update/{id}", 91L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDtoJson))
                .andExpect(status().isOk());
    }
}