package com.jbd.stock.repository;

import com.jbd.stock.domain.TypeCharge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeChargeRepository extends JpaRepository<TypeCharge, Long>, JpaSpecificationExecutor<TypeCharge> {}
