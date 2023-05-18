package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.repository.TypeChargeRepository;
import com.jbd.stock.service.criteria.TypeChargeCriteria;
import com.jbd.stock.service.dto.TypeChargeDTO;
import com.jbd.stock.service.mapper.TypeChargeMapper;
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
 * Service for executing complex queries for {@link TypeCharge} entities in the database.
 * The main input is a {@link TypeChargeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeChargeDTO} or a {@link Page} of {@link TypeChargeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeChargeQueryService extends QueryService<TypeCharge> {

    private final Logger log = LoggerFactory.getLogger(TypeChargeQueryService.class);

    private final TypeChargeRepository typeChargeRepository;

    private final TypeChargeMapper typeChargeMapper;

    public TypeChargeQueryService(TypeChargeRepository typeChargeRepository, TypeChargeMapper typeChargeMapper) {
        this.typeChargeRepository = typeChargeRepository;
        this.typeChargeMapper = typeChargeMapper;
    }

    /**
     * Return a {@link List} of {@link TypeChargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeChargeDTO> findByCriteria(TypeChargeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeCharge> specification = createSpecification(criteria);
        return typeChargeMapper.toDto(typeChargeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeChargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeChargeDTO> findByCriteria(TypeChargeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeCharge> specification = createSpecification(criteria);
        return typeChargeRepository.findAll(specification, page).map(typeChargeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeChargeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeCharge> specification = createSpecification(criteria);
        return typeChargeRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeChargeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeCharge> createSpecification(TypeChargeCriteria criteria) {
        Specification<TypeCharge> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeCharge_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), TypeCharge_.nom));
            }
            if (criteria.getChargeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChargeId(), root -> root.join(TypeCharge_.charges, JoinType.LEFT).get(Charge_.id))
                    );
            }
        }
        return specification;
    }
}
