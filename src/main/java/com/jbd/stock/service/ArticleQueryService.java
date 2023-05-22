package com.jbd.stock.service;

import com.jbd.stock.domain.*;
import com.jbd.stock.repository.ArticleRepository;
import com.jbd.stock.service.criteria.ArticleCriteria;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.mapper.ArticleMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleDTO} or a {@link Page} of {@link ArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    /**
     * Return a {@link List} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleMapper.toDto(articleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param statusF
     * @param search
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findByCriteria(ArticleCriteria criteria, Long statusF, String search, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        Specification<Article> specification = createSpecification(criteria);

        if (statusF != null && statusF != 0) {
            specification = specification.and(hasCategory(statusF));
        }

        if (search != null && !search.isEmpty()) {
            Specification<Article> specificationSertch = Specification.where(null);

            specificationSertch = specificationSertch.or(hasWord("nom", search.toLowerCase()));

            if (NumberUtils.isCreatable(search.trim())) {
                specificationSertch = specificationSertch.or(hasWord("code", search.toLowerCase()));
            }

            specification = specification.and(specificationSertch);
        }
        return articleRepository.findAll(specification, page).map(articleMapper::toDto);
    }

    public static Specification<Article> hasCategory(Long isCategory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(isCategory);
    }

    public static Specification<Article> hadId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(id);
    }

    private Specification<Article> hasWord(String column, String search) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get(column)), "%" + search.toLowerCase() + "%");
    }

    public static Specification<Article> hasLogin(String login) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("login")).value(login);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Article_.code));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Article_.nom));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Article_.description));
            }
            if (criteria.getPrixAchat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixAchat(), Article_.prixAchat));
            }
            if (criteria.getQte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQte(), Article_.qte));
            }
            if (criteria.getQteAlert() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQteAlert(), Article_.qteAlert));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Article_.status));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Article_.dateCreation));
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(Article_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getBonReceptionItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBonReceptionItemId(),
                            root -> root.join(Article_.bonReceptionItems, JoinType.LEFT).get(BonReceptionItem_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Article_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
            if (criteria.getFournisseurId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFournisseurId(),
                            root -> root.join(Article_.fournisseur, JoinType.LEFT).get(Fournisseur_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
