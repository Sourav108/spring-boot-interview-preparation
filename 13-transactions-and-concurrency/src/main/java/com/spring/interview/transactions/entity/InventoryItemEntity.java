package com.spring.interview.transactions.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "available_stock", nullable = false)
    private int availableStock;

    // Optimistic locking version field!
    @Version
    private Long version;

    public InventoryItemEntity() {}

    public InventoryItemEntity(String sku, int availableStock) {
        this.sku = sku;
        this.availableStock = availableStock;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }
    public Long getVersion() { return version; }
}
