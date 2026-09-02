package com.spring.interview.cache.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductCatalogCacheServiceTest {

    @Autowired
    private ProductCatalogCacheService productService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        productService.resetCounter();
        productService.evictAllProducts();
    }

    @Test
    @DisplayName("Should cache getProductById result and avoid repeated database queries on cache hit")
    void shouldCacheProductLookup() {
        // First call: Cache miss -> executes database query
        var p1 = productService.getProductById("prod-100");
        assertThat(p1).isNotNull();
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(1);

        // Second call: Cache hit -> should NOT increment database query count
        var p2 = productService.getProductById("prod-100");
        assertThat(p2).isNotNull();
        assertThat(p2.name()).isEqualTo("Product-prod-100");
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(1);

        // Third call for different product: Cache miss -> increments query count
        var p3 = productService.getProductById("prod-200");
        assertThat(p3).isNotNull();
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should evict item from cache and trigger fresh database query on subsequent lookup")
    void shouldEvictCacheEntry() {
        productService.getProductById("prod-300");
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(1);

        // Evict specific key
        productService.evictProduct("prod-300");

        // Next call must be a cache miss -> re-executes query
        productService.getProductById("prod-300");
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update cache value when @CachePut is invoked")
    void shouldUpdateCacheEntry() {
        productService.getProductById("prod-400");
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(1);

        // Update product in cache
        var updated = new ProductCatalogCacheService.Product("prod-400", "Updated Product", 199.99);
        productService.updateProduct(updated);

        // Subsequent lookup gets updated cached value without hitting DB
        var cached = productService.getProductById("prod-400");
        assertThat(cached.name()).isEqualTo("Updated Product");
        assertThat(cached.price()).isEqualTo(199.99);
        assertThat(productService.getDatabaseQueryCount()).isEqualTo(1);
    }
}
