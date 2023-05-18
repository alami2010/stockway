package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.repository.BonReceptionItemRepository;
import com.jbd.stock.service.criteria.BonReceptionItemCriteria;
import com.jbd.stock.service.dto.BonReceptionItemDTO;
import com.jbd.stock.service.mapper.BonReceptionItemMapper;
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
 * Service for executing complex queries for {@link BonReceptionItem} entities in the database.
 * The main input is a {@link BonReceptionItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BonReceptionItemDTO} or a {@link Page} of {@link BonReceptionItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BonReceptionItemQueryService extends QueryService<BonReceptionItem> {

    private final Logger log = LoggerFactory.getLogger(BonReceptionItemQueryService.class);

    private final BonReceptionItemRepository bonReceptionItemRepository;

    private final BonReceptionItemMapper bonReceptionItemMapper;

    public BonReceptionItemQueryService(
        BonReceptionItemRepository bonReceptionItemRepository,
        BonReceptionItemMapper bonReceptionItemMapper
    ) {
        this.bonReceptionItemRepository = bonReceptionItemRepository;
        this.bonReceptionItemMapper = bonReceptionItemMapper;
    }

    /**
     * Return a {@link List} of {@link BonReceptionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BonReceptionItemDTO> findByCriteria(BonReceptionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BonReceptionItem> specification = createSpecification(criteria);
        return bonReceptionItemMapper.toDto(bonReceptionItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BonReceptionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BonReceptionItemDTO> findByCriteria(BonReceptionItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BonReceptionItem> specification = createSpecification(criteria);
        return bonReceptionItemRepository.findAll(specification, page).map(bonReceptionItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BonReceptionItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BonReceptionItem> specification = createSpecification(criteria);
        return bonReceptionItemRepository.count(specification);
    }

    /**
     * Function to convert {@link BonReceptionItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BonReceptionItem> createSpecification(BonReceptionItemCriteria criteria) {
        Specification<BonReceptionItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BonReceptionItem_.id));
            }
            if (criteria.getQte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQte(), BonReceptionItem_.qte));
            }
            if (criteria.getBonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBonId(),
                            root -> root.join(BonReceptionItem_.bon, JoinType.LEFT).get(BonReception_.id)
                        )
                    );
            }
            if (criteria.getArticleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getArticleId(),
                            root -> root.join(BonReceptionItem_.article, JoinType.LEFT).get(Article_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
