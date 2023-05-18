package com.jbd.stock.service;

import com.jbd.stock.service.dto.ChargeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.jbd.stock.domain.Charge}.
 */
public interface ChargeService {
    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save.
     * @return the persisted entity.
     */
    ChargeDTO save(ChargeDTO chargeDTO);

    /**
     * Updates a charge.
     *
     * @param chargeDTO the entity to update.
     * @return the persisted entity.
     */
    ChargeDTO update(ChargeDTO chargeDTO);

    /**
     * Partially updates a charge.
     *
     * @param chargeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChargeDTO> partialUpdate(ChargeDTO chargeDTO);

    /**
     * Get all the charges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChargeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" charge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChargeDTO> findOne(Long id);

    /**
     * Delete the "id" charge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
