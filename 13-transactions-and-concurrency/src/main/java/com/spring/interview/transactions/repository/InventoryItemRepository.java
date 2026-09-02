package com.spring.interview.transactions.repository;

import com.spring.interview.transactions.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {
    Optional<InventoryItemEntity> findBySku(String sku);
}
