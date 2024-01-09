package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock
    private AuditService service;

    @InjectMocks
    private AuditController controller;

    private MockMvc mockMvc;

    private static AuditDto dto;

    @BeforeAll
    static void prepare() {
        dto = new AuditDto(
                1L,
                "Test entity type",
                "Test operation type",
                "Test creator",
                "Test modifier",
                new Timestamp(77L),
                new Timestamp(100L),
                "Test new entity json",
                "Test entity json"
        );
    }

    @BeforeEach
    void eachPrepare() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Чтение одного по id, позитивный сценарий")
    void readPositiveTest() throws Exception {
        Mockito.doReturn(dto).when(service).findById(1L);

        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Чтение одного по несуществующему id, негативный сценарий")
    void readNegativeTest() throws Exception {
        Mockito.doReturn(null).when(service).findById(8L);

        mockMvc.perform(get("/audit/{id}", 8L))
                .andExpect(status().isOk());
    }
}