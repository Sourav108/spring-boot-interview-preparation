package com.spring.interview.transactions.service;

import com.spring.interview.transactions.entity.InventoryItemEntity;
import com.spring.interview.transactions.repository.InventoryItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OptimisticLockingInventoryServiceTest {

    @Autowired
    private OptimisticLockingInventoryService inventoryService;

    @Autowired
    private InventoryItemRepository inventoryRepository;

    @Test
    @DisplayName("Should successfully decrement inventory stock and increment @Version column")
    void shouldReserveInventoryAndIncrementVersion() {
        var item = inventoryRepository.save(new InventoryItemEntity("SKU-PRO-MAX", 50));
        Long initialVersion = item.getVersion();

        boolean reserved = inventoryService.reserveItemWithRetry(item.getId(), 5, 3);

        assertThat(reserved).isTrue();
        var updated = inventoryRepository.findById(item.getId()).orElseThrow();
        assertThat(updated.getAvailableStock()).isEqualTo(45);
        assertThat(updated.getVersion()).isGreaterThanOrEqualTo(initialVersion);
    }
}
