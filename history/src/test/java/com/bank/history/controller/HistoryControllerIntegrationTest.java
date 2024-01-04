package com.bank.history.controller;

import com.bank.history.entity.HistoryEntity;
import com.bank.history.repository.HistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import javax.persistence.EntityNotFoundException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HistoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HistoryRepository historyRepository;

    @AfterEach
    void resetDb() {
        historyRepository.deleteAll();
    }

    Long createDefaultHistory() {
        HistoryEntity defaultHistory = new HistoryEntity();
        defaultHistory.setTransferAuditId(777L);
        defaultHistory.setProfileAuditId(777L);
        defaultHistory.setAccountAuditId(777L);
        defaultHistory.setAntiFraudAuditId(777L);
        defaultHistory.setPublicBankInfoAuditId(777L);
        defaultHistory.setAuthorizationAuditId(777L);
        return historyRepository.save(defaultHistory).getId();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        mockMvc.perform(get("/api/history/{id}", createDefaultHistory()))
                .andExpectAll(status().isOk(),
                        content().json("""
                                {
                                    "transferAuditId":777,
                                    "profileAuditId":777,
                                    "accountAuditId":777,
                                    "antiFraudAuditId":777,
                                    "publicBankInfoAuditId":777,
                                    "authorizationAuditId":777                        
                                }
                                """));
    }

    @Test
    @DisplayName("чтение по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() throws Exception {
        mockMvc.perform(get("/api/history/{id}", 1L))
                .andExpectAll(status().isNotFound(),
                        result -> result.getResolvedException().getClass()
                                .equals(EntityNotFoundException.class));
    }

    @Test
    @DisplayName("чтение по списку из id, позитивный сценарий")
    void readAllByListOfIdPositiveTest() throws Exception {
        mockMvc.perform(get("/api/history").param("id", createDefaultHistory().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                        "transferAuditId":777,
                        "profileAuditId":777,
                        "accountAuditId":777,
                        "antiFraudAuditId":777,
                        "publicBankInfoAuditId":777,
                        "authorizationAuditId":777
                        }]
                        """));
    }

    @Test
    @DisplayName("чтение по списку из несуществующих id, негативный сценарий")
    void readAllByListOfNonExistIdNegativeTest() throws Exception {
        mockMvc.perform(get("/api/history").param("id", "1"))
                .andExpectAll(status().isNotFound(),
                        result -> result.getResolvedException().getClass()
                                .equals(EntityNotFoundException.class));
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createValidPositiveTest() throws Exception {
        mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "transferAuditId": 888,
                            "profileAuditId": 888,
                            "accountAuditId": 888,
                            "antiFraudAuditId": 888,
                            "publicBankInfoAuditId": 888,
                            "authorizationAuditId": 888
                        }
                        """))
                .andExpectAll(status().isOk(),
                        content().json("""
                                {
                                    "transferAuditId": 888,
                                    "profileAuditId": 888,
                                    "accountAuditId": 888,
                                    "antiFraudAuditId": 888,
                                    "publicBankInfoAuditId": 888,
                                    "authorizationAuditId": 888
                                }        
                                """));
    }

    @Test
    @DisplayName("создание с не валидными полями, негативный сценарий")
    void createByInvalidFieldsNegativeTest() throws Exception {
        mockMvc.perform(post("/api/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "transferAuditId":"a",
                            "profileAuditId":"a",
                            "accountAuditId":"a",
                            "antiFraudAuditId":"a",
                            "publicBankInfoAuditId":"a",
                            "authorizationAuditId":"a"
                        }
                        """))
                .andExpectAll(status().isBadRequest(),
                        result -> result.getResolvedException().getClass()
                                .equals(HttpMessageNotReadableException.class));
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception {
        mockMvc.perform(put("/api/history/{id}", createDefaultHistory())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "transferAuditId": 111,
                        "profileAuditId": 111,
                        "accountAuditId": 111,
                        "antiFraudAuditId": 111,
                        "publicBankInfoAuditId": 111,
                        "authorizationAuditId": 111                        
                        }
                        """))
                .andExpectAll(status().isOk(),
                        content().json("""
                                {
                                "transferAuditId": 111,
                                "profileAuditId": 111,
                                "accountAuditId": 111,
                                "antiFraudAuditId": 111,
                                "publicBankInfoAuditId": 111,
                                "authorizationAuditId": 111                        
                                }
                                        """));
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() throws Exception {
        mockMvc.perform(put("/api/history/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id": 1,
                        "transferAuditId": 1,
                        "profileAuditId": 1,
                        "accountAuditId": 1,
                        "antiFraudAuditId": 1,
                        "publicBankInfoAuditId": 1,
                        "authorizationAuditId": 1                        
                        }
                        """))
                .andExpectAll(status().isNotFound(),
                        result -> result.getResolvedException().getClass()
                                .equals(EntityNotFoundException.class));
    }
}