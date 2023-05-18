package com.jbd.stock.web.rest;

import com.jbd.stock.repository.BonReceptionItemRepository;
import com.jbd.stock.service.BonReceptionItemQueryService;
import com.jbd.stock.service.BonReceptionItemService;
import com.jbd.stock.service.criteria.BonReceptionItemCriteria;
import com.jbd.stock.service.dto.BonReceptionItemDTO;
import com.jbd.stock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jbd.stock.domain.BonReceptionItem}.
 */
@RestController
@RequestMapping("/api")
public class BonReceptionItemResource {

    private final Logger log = LoggerFactory.getLogger(BonReceptionItemResource.class);

    private static final String ENTITY_NAME = "bonReceptionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonReceptionItemService bonReceptionItemService;

    private final BonReceptionItemRepository bonReceptionItemRepository;

    private final BonReceptionItemQueryService bonReceptionItemQueryService;

    public BonReceptionItemResource(
        BonReceptionItemService bonReceptionItemService,
        BonReceptionItemRepository bonReceptionItemRepository,
        BonReceptionItemQueryService bonReceptionItemQueryService
    ) {
        this.bonReceptionItemService = bonReceptionItemService;
        this.bonReceptionItemRepository = bonReceptionItemRepository;
        this.bonReceptionItemQueryService = bonReceptionItemQueryService;
    }

    /**
     * {@code POST  /bon-reception-items} : Create a new bonReceptionItem.
     *
     * @param bonReceptionItemDTO the bonReceptionItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bonReceptionItemDTO, or with status {@code 400 (Bad Request)} if the bonReceptionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bon-reception-items")
    public ResponseEntity<BonReceptionItemDTO> createBonReceptionItem(@RequestBody BonReceptionItemDTO bonReceptionItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save BonReceptionItem : {}", bonReceptionItemDTO);
        if (bonReceptionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new bonReceptionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BonReceptionItemDTO result = bonReceptionItemService.save(bonReceptionItemDTO);
        return ResponseEntity
            .created(new URI("/api/bon-reception-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bon-reception-items/:id} : Updates an existing bonReceptionItem.
     *
     * @param id the id of the bonReceptionItemDTO to save.
     * @param bonReceptionItemDTO the bonReceptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonReceptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the bonReceptionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bonReceptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bon-reception-items/{id}")
    public ResponseEntity<BonReceptionItemDTO> updateBonReceptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BonReceptionItemDTO bonReceptionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BonReceptionItem : {}, {}", id, bonReceptionItemDTO);
        if (bonReceptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonReceptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonReceptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BonReceptionItemDTO result = bonReceptionItemService.update(bonReceptionItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bonReceptionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bon-reception-items/:id} : Partial updates given fields of an existing bonReceptionItem, field will ignore if it is null
     *
     * @param id the id of the bonReceptionItemDTO to save.
     * @param bonReceptionItemDTO the bonReceptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonReceptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the bonReceptionItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bonReceptionItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bonReceptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bon-reception-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BonReceptionItemDTO> partialUpdateBonReceptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BonReceptionItemDTO bonReceptionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BonReceptionItem partially : {}, {}", id, bonReceptionItemDTO);
        if (bonReceptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonReceptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonReceptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BonReceptionItemDTO> result = bonReceptionItemService.partialUpdate(bonReceptionItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bonReceptionItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bon-reception-items} : get all the bonReceptionItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bonReceptionItems in body.
     */
    @GetMapping("/bon-reception-items")
    public ResponseEntity<List<BonReceptionItemDTO>> getAllBonReceptionItems(BonReceptionItemCriteria criteria) {
        log.debug("REST request to get BonReceptionItems by criteria: {}", criteria);
        List<BonReceptionItemDTO> entityList = bonReceptionItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /bon-reception-items/count} : count all the bonReceptionItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bon-reception-items/count")
    public ResponseEntity<Long> countBonReceptionItems(BonReceptionItemCriteria criteria) {
        log.debug("REST request to count BonReceptionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(bonReceptionItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bon-reception-items/:id} : get the "id" bonReceptionItem.
     *
     * @param id the id of the bonReceptionItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bonReceptionItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bon-reception-items/{id}")
    public ResponseEntity<BonReceptionItemDTO> getBonReceptionItem(@PathVariable Long id) {
        log.debug("REST request to get BonReceptionItem : {}", id);
        Optional<BonReceptionItemDTO> bonReceptionItemDTO = bonReceptionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bonReceptionItemDTO);
    }

    /**
     * {@code DELETE  /bon-reception-items/:id} : delete the "id" bonReceptionItem.
     *
     * @param id the id of the bonReceptionItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bon-reception-items/{id}")
    public ResponseEntity<Void> deleteBonReceptionItem(@PathVariable Long id) {
        log.debug("REST request to delete BonReceptionItem : {}", id);
        bonReceptionItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
