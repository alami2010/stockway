package com.jbd.stock.service;

import com.jbd.stock.service.dto.TypeChargeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.jbd.stock.domain.TypeCharge}.
 */
public interface TypeChargeService {
    /**
     * Save a typeCharge.
     *
     * @param typeChargeDTO the entity to save.
     * @return the persisted entity.
     */
    TypeChargeDTO save(TypeChargeDTO typeChargeDTO);

    /**
     * Updates a typeCharge.
     *
     * @param typeChargeDTO the entity to update.
     * @return the persisted entity.
     */
    TypeChargeDTO update(TypeChargeDTO typeChargeDTO);

    /**
     * Partially updates a typeCharge.
     *
     * @param typeChargeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeChargeDTO> partialUpdate(TypeChargeDTO typeChargeDTO);

    /**
     * Get all the typeCharges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeChargeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeCharge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeChargeDTO> findOne(Long id);

    /**
     * Delete the "id" typeCharge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
