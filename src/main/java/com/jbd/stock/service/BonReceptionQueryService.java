package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.BonReception;
import com.jbd.stock.repository.BonReceptionRepository;
import com.jbd.stock.service.criteria.BonReceptionCriteria;
import com.jbd.stock.service.dto.BonReceptionDTO;
import com.jbd.stock.service.mapper.BonReceptionMapper;
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
 * Service for executing complex queries for {@link BonReception} entities in the database.
 * The main input is a {@link BonReceptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BonReceptionDTO} or a {@link Page} of {@link BonReceptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BonReceptionQueryService extends QueryService<BonReception> {

    private final Logger log = LoggerFactory.getLogger(BonReceptionQueryService.class);

    private final BonReceptionRepository bonReceptionRepository;

    private final BonReceptionMapper bonReceptionMapper;

    public BonReceptionQueryService(BonReceptionRepository bonReceptionRepository, BonReceptionMapper bonReceptionMapper) {
        this.bonReceptionRepository = bonReceptionRepository;
        this.bonReceptionMapper = bonReceptionMapper;
    }

    /**
     * Return a {@link List} of {@link BonReceptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BonReceptionDTO> findByCriteria(BonReceptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BonReception> specification = createSpecification(criteria);
        return bonReceptionMapper.toDto(bonReceptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BonReceptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BonReceptionDTO> findByCriteria(BonReceptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BonReception> specification = createSpecification(criteria);
        return bonReceptionRepository.findAll(specification, page).map(bonReceptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BonReceptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BonReception> specification = createSpecification(criteria);
        return bonReceptionRepository.count(specification);
    }

    /**
     * Function to convert {@link BonReceptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BonReception> createSpecification(BonReceptionCriteria criteria) {
        Specification<BonReception> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BonReception_.id));
            }
            if (criteria.getInformaton() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInformaton(), BonReception_.informaton));
            }
            if (criteria.getNumFacture() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumFacture(), BonReception_.numFacture));
            }
            if (criteria.getNumBl() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumBl(), BonReception_.numBl));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), BonReception_.dateCreation));
            }
            if (criteria.getBonReceptionItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBonReceptionItemId(),
                            root -> root.join(BonReception_.bonReceptionItems, JoinType.LEFT).get(BonReceptionItem_.id)
                        )
                    );
            }
            if (criteria.getFournisseurId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFournisseurId(),
                            root -> root.join(BonReception_.fournisseur, JoinType.LEFT).get(Fournisseur_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
