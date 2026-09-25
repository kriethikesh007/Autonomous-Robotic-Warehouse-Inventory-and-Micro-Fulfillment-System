package com.warehouse.wms.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.wms.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsByProductIdAndBinId(Long productId, Long binId);

    boolean existsByProductIdAndBinIdAndIdNot(
            Long productId,
            Long binId,
            Long id);
}