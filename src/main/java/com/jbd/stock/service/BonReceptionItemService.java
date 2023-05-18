package com.jbd.stock.service;

import com.jbd.stock.service.dto.BonReceptionItemDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.jbd.stock.domain.BonReceptionItem}.
 */
public interface BonReceptionItemService {
    /**
     * Save a bonReceptionItem.
     *
     * @param bonReceptionItemDTO the entity to save.
     * @return the persisted entity.
     */
    BonReceptionItemDTO save(BonReceptionItemDTO bonReceptionItemDTO);

    /**
     * Updates a bonReceptionItem.
     *
     * @param bonReceptionItemDTO the entity to update.
     * @return the persisted entity.
     */
    BonReceptionItemDTO update(BonReceptionItemDTO bonReceptionItemDTO);

    /**
     * Partially updates a bonReceptionItem.
     *
     * @param bonReceptionItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BonReceptionItemDTO> partialUpdate(BonReceptionItemDTO bonReceptionItemDTO);

    /**
     * Get all the bonReceptionItems.
     *
     * @return the list of entities.
     */
    List<BonReceptionItemDTO> findAll();

    /**
     * Get the "id" bonReceptionItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BonReceptionItemDTO> findOne(Long id);

    /**
     * Delete the "id" bonReceptionItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
