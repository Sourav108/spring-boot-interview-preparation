package com.spring.interview.transactions.service;

import com.spring.interview.transactions.entity.InventoryItemEntity;
import com.spring.interview.transactions.repository.InventoryItemRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockingInventoryService {

    private final InventoryItemRepository repository;

    public OptimisticLockingInventoryService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    public boolean reserveItemWithRetry(Long itemId, int quantity, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return executeReservation(itemId, quantity);
            } catch (OptimisticLockingFailureException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw e;
                }
            }
        }
        return false;
    }

    @Transactional
    public boolean executeReservation(Long itemId, int quantity) {
        InventoryItemEntity item = repository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getAvailableStock() < quantity) {
            return false;
        }

        item.setAvailableStock(item.getAvailableStock() - quantity);
        repository.save(item);
        return true;
    }
}
