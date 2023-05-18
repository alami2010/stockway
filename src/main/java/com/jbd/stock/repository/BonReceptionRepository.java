package com.jbd.stock.repository;

import com.jbd.stock.domain.BonReception;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BonReception entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonReceptionRepository extends JpaRepository<BonReception, Long>, JpaSpecificationExecutor<BonReception> {}
