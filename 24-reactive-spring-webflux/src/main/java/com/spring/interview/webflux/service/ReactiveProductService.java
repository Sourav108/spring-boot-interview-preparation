package com.spring.interview.webflux.service;

import com.spring.interview.webflux.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class ReactiveProductService {

    // ConcurrentSkipListMap guarantees natural key ordering (prod-1, prod-2, prod-3)
    private final ConcurrentSkipListMap<String, Product> productDb = new ConcurrentSkipListMap<>();

    public ReactiveProductService() {
        productDb.put("prod-1", new Product("prod-1", "MacBook Pro", 2499.0));
        productDb.put("prod-2", new Product("prod-2", "Magic Keyboard", 199.0));
        productDb.put("prod-3", new Product("prod-3", "Studio Display", 1599.0));
    }

    public Mono<Product> findById(String id) {
        if ("INVALID".equals(id)) {
            return Mono.error(new IllegalArgumentException("Invalid product id supplied"));
        }
        Product product = productDb.get(id);
        return product != null ? Mono.just(product) : Mono.empty();
    }

    public Flux<Product> findAll() {
        return Flux.fromIterable(productDb.values())
            .map(p -> new Product(p.id(), p.name().toUpperCase(), p.price()));
    }

    public Mono<Product> createProduct(Product product) {
        productDb.put(product.id(), product);
        return Mono.just(product);
    }
}
