package com.jbd.stock.repository;

import com.jbd.stock.domain.BonReceptionItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BonReceptionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonReceptionItemRepository extends JpaRepository<BonReceptionItem, Long>, JpaSpecificationExecutor<BonReceptionItem> {}
