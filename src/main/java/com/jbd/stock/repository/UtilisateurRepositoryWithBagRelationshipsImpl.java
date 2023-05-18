package com.jbd.stock.repository;

import com.jbd.stock.domain.Utilisateur;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UtilisateurRepositoryWithBagRelationshipsImpl implements UtilisateurRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Utilisateur> fetchBagRelationships(Optional<Utilisateur> utilisateur) {
        return utilisateur.map(this::fetchRoles);
    }

    @Override
    public Page<Utilisateur> fetchBagRelationships(Page<Utilisateur> utilisateurs) {
        return new PageImpl<>(
            fetchBagRelationships(utilisateurs.getContent()),
            utilisateurs.getPageable(),
            utilisateurs.getTotalElements()
        );
    }

    @Override
    public List<Utilisateur> fetchBagRelationships(List<Utilisateur> utilisateurs) {
        return Optional.of(utilisateurs).map(this::fetchRoles).orElse(Collections.emptyList());
    }

    Utilisateur fetchRoles(Utilisateur result) {
        return entityManager
            .createQuery(
                "select utilisateur from Utilisateur utilisateur left join fetch utilisateur.roles where utilisateur is :utilisateur",
                Utilisateur.class
            )
            .setParameter("utilisateur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Utilisateur> fetchRoles(List<Utilisateur> utilisateurs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, utilisateurs.size()).forEach(index -> order.put(utilisateurs.get(index).getId(), index));
        List<Utilisateur> result = entityManager
            .createQuery(
                "select distinct utilisateur from Utilisateur utilisateur left join fetch utilisateur.roles where utilisateur in :utilisateurs",
                Utilisateur.class
            )
            .setParameter("utilisateurs", utilisateurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
