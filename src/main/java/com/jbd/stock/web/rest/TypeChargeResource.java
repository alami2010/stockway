package com.jbd.stock.web.rest;

import com.jbd.stock.repository.TypeChargeRepository;
import com.jbd.stock.service.TypeChargeQueryService;
import com.jbd.stock.service.TypeChargeService;
import com.jbd.stock.service.criteria.TypeChargeCriteria;
import com.jbd.stock.service.dto.TypeChargeDTO;
import com.jbd.stock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jbd.stock.domain.TypeCharge}.
 */
@RestController
@RequestMapping("/api")
public class TypeChargeResource {

    private final Logger log = LoggerFactory.getLogger(TypeChargeResource.class);

    private static final String ENTITY_NAME = "typeCharge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeChargeService typeChargeService;

    private final TypeChargeRepository typeChargeRepository;

    private final TypeChargeQueryService typeChargeQueryService;

    public TypeChargeResource(
        TypeChargeService typeChargeService,
        TypeChargeRepository typeChargeRepository,
        TypeChargeQueryService typeChargeQueryService
    ) {
        this.typeChargeService = typeChargeService;
        this.typeChargeRepository = typeChargeRepository;
        this.typeChargeQueryService = typeChargeQueryService;
    }

    /**
     * {@code POST  /type-charges} : Create a new typeCharge.
     *
     * @param typeChargeDTO the typeChargeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeChargeDTO, or with status {@code 400 (Bad Request)} if the typeCharge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-charges")
    public ResponseEntity<TypeChargeDTO> createTypeCharge(@RequestBody TypeChargeDTO typeChargeDTO) throws URISyntaxException {
        log.debug("REST request to save TypeCharge : {}", typeChargeDTO);
        if (typeChargeDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeCharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeChargeDTO result = typeChargeService.save(typeChargeDTO);
        return ResponseEntity
            .created(new URI("/api/type-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-charges/:id} : Updates an existing typeCharge.
     *
     * @param id the id of the typeChargeDTO to save.
     * @param typeChargeDTO the typeChargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeChargeDTO,
     * or with status {@code 400 (Bad Request)} if the typeChargeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeChargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-charges/{id}")
    public ResponseEntity<TypeChargeDTO> updateTypeCharge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeChargeDTO typeChargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeCharge : {}, {}", id, typeChargeDTO);
        if (typeChargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeChargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeChargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeChargeDTO result = typeChargeService.update(typeChargeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeChargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-charges/:id} : Partial updates given fields of an existing typeCharge, field will ignore if it is null
     *
     * @param id the id of the typeChargeDTO to save.
     * @param typeChargeDTO the typeChargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeChargeDTO,
     * or with status {@code 400 (Bad Request)} if the typeChargeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeChargeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeChargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-charges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeChargeDTO> partialUpdateTypeCharge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeChargeDTO typeChargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeCharge partially : {}, {}", id, typeChargeDTO);
        if (typeChargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeChargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeChargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeChargeDTO> result = typeChargeService.partialUpdate(typeChargeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeChargeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-charges} : get all the typeCharges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeCharges in body.
     */
    @GetMapping("/type-charges")
    public ResponseEntity<List<TypeChargeDTO>> getAllTypeCharges(
        TypeChargeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TypeCharges by criteria: {}", criteria);
        Page<TypeChargeDTO> page = typeChargeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-charges/count} : count all the typeCharges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-charges/count")
    public ResponseEntity<Long> countTypeCharges(TypeChargeCriteria criteria) {
        log.debug("REST request to count TypeCharges by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeChargeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-charges/:id} : get the "id" typeCharge.
     *
     * @param id the id of the typeChargeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeChargeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-charges/{id}")
    public ResponseEntity<TypeChargeDTO> getTypeCharge(@PathVariable Long id) {
        log.debug("REST request to get TypeCharge : {}", id);
        Optional<TypeChargeDTO> typeChargeDTO = typeChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeChargeDTO);
    }

    /**
     * {@code DELETE  /type-charges/:id} : delete the "id" typeCharge.
     *
     * @param id the id of the typeChargeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-charges/{id}")
    public ResponseEntity<Void> deleteTypeCharge(@PathVariable Long id) {
        log.debug("REST request to delete TypeCharge : {}", id);
        typeChargeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
