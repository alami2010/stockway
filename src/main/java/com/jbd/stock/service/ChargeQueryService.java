package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.Charge;
import com.jbd.stock.repository.ChargeRepository;
import com.jbd.stock.service.criteria.ChargeCriteria;
import com.jbd.stock.service.dto.ChargeDTO;
import com.jbd.stock.service.mapper.ChargeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Charge} entities in the database.
 * The main input is a {@link ChargeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChargeDTO} or a {@link Page} of {@link ChargeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChargeQueryService extends QueryService<Charge> {

    private final Logger log = LoggerFactory.getLogger(ChargeQueryService.class);

    private final ChargeRepository chargeRepository;

    private final ChargeMapper chargeMapper;

    public ChargeQueryService(ChargeRepository chargeRepository, ChargeMapper chargeMapper) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
    }

    /**
     * Return a {@link List} of {@link ChargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChargeDTO> findByCriteria(ChargeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Charge> specification = createSpecification(criteria);
        return chargeMapper.toDto(chargeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChargeDTO> findByCriteria(ChargeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Charge> specification = createSpecification(criteria);
        return chargeRepository.findAll(specification, page).map(chargeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChargeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Charge> specification = createSpecification(criteria);
        return chargeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChargeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Charge> createSpecification(ChargeCriteria criteria) {
        Specification<Charge> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Charge_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Charge_.nom));
            }
            if (criteria.getValeur() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValeur(), Charge_.valeur));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Charge_.dateCreation));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypeId(), root -> root.join(Charge_.type, JoinType.LEFT).get(TypeCharge_.id))
                    );
            }
        }
        return specification;
    }
}
