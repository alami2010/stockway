package com.jbd.stock.service;

import com.jbd.stock.service.dto.BonReceptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.jbd.stock.domain.BonReception}.
 */
public interface BonReceptionService {
    /**
     * Save a bonReception.
     *
     * @param bonReceptionDTO the entity to save.
     * @return the persisted entity.
     */
    BonReceptionDTO save(BonReceptionDTO bonReceptionDTO);

    /**
     * Updates a bonReception.
     *
     * @param bonReceptionDTO the entity to update.
     * @return the persisted entity.
     */
    BonReceptionDTO update(BonReceptionDTO bonReceptionDTO);

    /**
     * Partially updates a bonReception.
     *
     * @param bonReceptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BonReceptionDTO> partialUpdate(BonReceptionDTO bonReceptionDTO);

    /**
     * Get all the bonReceptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BonReceptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bonReception.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BonReceptionDTO> findOne(Long id);

    /**
     * Delete the "id" bonReception.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
