package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;
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
class BankDetailsControllerTest {

    @Mock
    private BankDetailsService service;

    @InjectMocks
    private BankDetailsController controller;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static BankDetailsDto dto;

    @BeforeAll
    static void prepare () {
        dto = new BankDetailsDto(
                5L,
                78L,
                66L,
                789L,
                new BigDecimal(8),
                "Test city",
                "Test joint stock company",
                "Test name"
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
        Mockito.doReturn(dto).when(service).findById(5L);

        mockMvc.perform(get("/bank/details/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    @DisplayName("Чтение одного по несуществующему id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        Mockito.doReturn(null).when(service).findById(99L);

        mockMvc.perform(get("/bank/details/{id}", 99L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Чтение всех по id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        BankDetailsDto dto1 = new BankDetailsDto();
        BankDetailsDto dto2 = new BankDetailsDto();
        dto1.setId(2L);
        dto2.setId(9L);
        List<BankDetailsDto> bankDetailsDtoList = List.of(dto, dto1, dto2);

        Mockito.doReturn(bankDetailsDtoList).when(service).findAllById(List.of(5L,2L,9L));

        mockMvc.perform(get("/bank/details/read/all")
                        .param("ids", "5", "2", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.containsInAnyOrder(5, 2, 9)));
    }

    @Test
    @DisplayName("Чтение всех по некорректному листу id, негативный сценарий")
    void readAllByIdNegativeTest() throws Exception {
        Mockito.doReturn(Collections.emptyList()).when(service).findAllById(List.of(78L,56L,536L));

        mockMvc.perform(get("/bank/details/read/all")
                        .param("ids", "78", "56", "536"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание нового, позитивный сценарий")
    void createPositiveTest() throws Exception {
        String BankDetailsDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(dto).when(service).create(dto);

        mockMvc.perform(post("/bank/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BankDetailsDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    @DisplayName("Создание нового, входной дто is null, негативный сценарий")
    void createWithNullDtoTest() throws Exception {
        String BankDetailsDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).create(dto);

        mockMvc.perform(post("/bank/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BankDetailsDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление данных, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        BankDetailsDto dto1 = new BankDetailsDto(
                22L,
                789L,
                555L,
                897L,
                new BigDecimal(89),
                "New test city",
                "New test joint stick company",
                "New test name"
        );
        String BankDetailsDto1Json = objectMapper.writeValueAsString(dto1);

        Mockito.doReturn(dto1).when(service).update(5L,dto1);

        mockMvc.perform(put("/bank/details/update/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BankDetailsDto1Json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto1.getId()));
    }

    @Test
    @DisplayName("Обновление данных о несуществующем объекте, негативный сценарий")
    void updateNegativeTest() throws  Exception {
        String BankDetailsDtoJson = objectMapper.writeValueAsString(dto);

        Mockito.doReturn(null).when(service).update(91L, dto);

        mockMvc.perform(put("/bank/details/update/{id}", 91L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BankDetailsDtoJson))
                .andExpect(status().isOk());
    }
}