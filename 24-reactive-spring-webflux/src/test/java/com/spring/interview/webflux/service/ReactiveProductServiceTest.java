package com.spring.interview.webflux.service;

import com.spring.interview.webflux.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ReactiveProductServiceTest {

    private final ReactiveProductService productService = new ReactiveProductService();

    @Test
    @DisplayName("Should emit Product and complete successfully for valid id using StepVerifier")
    void shouldFindProductById() {
        var mono = productService.findById("prod-1");

        StepVerifier.create(mono)
            .expectNextMatches(p -> "prod-1".equals(p.id()) && "MacBook Pro".equals(p.name()) && p.price() == 2499.0)
            .verifyComplete();
    }

    @Test
    @DisplayName("Should complete as empty Mono for non-existent product id")
    void shouldReturnEmptyForNonExistentProduct() {
        var mono = productService.findById("prod-nonexistent");

        StepVerifier.create(mono)
            .verifyComplete();
    }

    @Test
    @DisplayName("Should emit error signal when invalid product id supplied")
    void shouldEmitErrorOnInvalidId() {
        var mono = productService.findById("INVALID");

        StepVerifier.create(mono)
            .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException
                && throwable.getMessage().contains("Invalid product id"))
            .verify();
    }

    @Test
    @DisplayName("Should stream transformed upper-case products via Flux")
    void shouldStreamAllProducts() {
        var flux = productService.findAll();

        StepVerifier.create(flux)
            .expectNextMatches(p -> "MACBOOK PRO".equals(p.name()))
            .expectNextMatches(p -> "MAGIC KEYBOARD".equals(p.name()))
            .expectNextMatches(p -> "STUDIO DISPLAY".equals(p.name()))
            .verifyComplete();
    }
}
