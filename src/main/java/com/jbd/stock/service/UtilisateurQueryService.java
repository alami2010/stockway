package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.repository.UtilisateurRepository;
import com.jbd.stock.service.criteria.UtilisateurCriteria;
import com.jbd.stock.service.dto.UtilisateurDTO;
import com.jbd.stock.service.mapper.UtilisateurMapper;
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
 * Service for executing complex queries for {@link Utilisateur} entities in the database.
 * The main input is a {@link UtilisateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UtilisateurDTO} or a {@link Page} of {@link UtilisateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UtilisateurQueryService extends QueryService<Utilisateur> {

    private final Logger log = LoggerFactory.getLogger(UtilisateurQueryService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurQueryService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Return a {@link List} of {@link UtilisateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findByCriteria(UtilisateurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurMapper.toDto(utilisateurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UtilisateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UtilisateurDTO> findByCriteria(UtilisateurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurRepository.findAll(specification, page).map(utilisateurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UtilisateurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurRepository.count(specification);
    }

    /**
     * Function to convert {@link UtilisateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Utilisateur> createSpecification(UtilisateurCriteria criteria) {
        Specification<Utilisateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Utilisateur_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Utilisateur_.code));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Utilisateur_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Utilisateur_.prenom));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Utilisateur_.dateCreation));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Utilisateur_.status));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Utilisateur_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Utilisateur_.email));
            }
            if (criteria.getInformation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInformation(), Utilisateur_.information));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Utilisateur_.type));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Utilisateur_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getPaiementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaiementId(),
                            root -> root.join(Utilisateur_.paiements, JoinType.LEFT).get(Paiement_.id)
                        )
                    );
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(Utilisateur_.orders, JoinType.LEFT).get(Order_.id))
                    );
            }
            if (criteria.getRolesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRolesId(), root -> root.join(Utilisateur_.roles, JoinType.LEFT).get(Role_.id))
                    );
            }
        }
        return specification;
    }
}
