package com.bank.publicinfo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EntityNotFoundSupplierTest {
    private final EntityNotFoundSupplier supplier;

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
        assertThrows(EntityNotFoundException.class,
                () -> supplier.checkForSizeAndLogging("Test", List.of(1L,2L,3L), Collections.emptyList()));
    }
}