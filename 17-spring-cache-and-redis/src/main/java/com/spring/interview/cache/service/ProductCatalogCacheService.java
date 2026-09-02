package com.spring.interview.cache.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service demonstrating Spring Cache proxy annotations: @Cacheable, @CachePut, and @CacheEvict with invocation counters.
 */
@Service
public class ProductCatalogCacheService {

    public record Product(String id, String name, double price) {}

    private final AtomicInteger databaseQueryCounter = new AtomicInteger(0);

    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public Product getProductById(String id) {
        // Simulates expensive database query
        databaseQueryCounter.incrementAndGet();
        return new Product(id, "Product-" + id, 99.99);
    }

    @CachePut(value = "products", key = "#product.id()")
    public Product updateProduct(Product product) {
        // Simulates updating database and updating the cache simultaneously
        return product;
    }

    @CacheEvict(value = "products", key = "#id")
    public void evictProduct(String id) {
        // Evicts specific product key from cache
    }

    @CacheEvict(value = "products", allEntries = true)
    public void evictAllProducts() {
        // Evicts entire cache
    }

    public int getDatabaseQueryCount() {
        return databaseQueryCounter.get();
    }

    public void resetCounter() {
        databaseQueryCounter.set(0);
    }
}
