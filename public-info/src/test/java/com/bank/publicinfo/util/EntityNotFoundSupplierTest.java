package com.bank.publicinfo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntityNotFoundSupplierTest {
    private EntityNotFoundSupplier supplier;

   @Autowired
    public EntityNotFoundSupplierTest(EntityNotFoundSupplier supplier) {
        this.supplier = supplier;
    }

    @Test
    @DisplayName("Выброс EntityNotFoundException")
    void getExceptionTest() {
        Exception suppliedException = supplier.getException("Test", 1L);

        assertThat(suppliedException.getClass()).isEqualTo(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Выброс EntityNotFoundException при сравнении листов id и entities")
    void checkForSizeAndLoggingTest() {
       assertThrows(EntityNotFoundException.class, () -> supplier.checkForSizeAndLogging("Test", List.of(1L,2L,3L),
               Collections.emptyList()));
   }
}