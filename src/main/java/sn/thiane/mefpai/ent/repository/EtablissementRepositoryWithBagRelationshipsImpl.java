package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.thiane.mefpai.ent.domain.Etablissement;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtablissementRepositoryWithBagRelationshipsImpl implements EtablissementRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Etablissement> fetchBagRelationships(Optional<Etablissement> etablissement) {
        return etablissement.map(this::fetchRessources);
    }

    @Override
    public Page<Etablissement> fetchBagRelationships(Page<Etablissement> etablissements) {
        return new PageImpl<>(
            fetchBagRelationships(etablissements.getContent()),
            etablissements.getPageable(),
            etablissements.getTotalElements()
        );
    }

    @Override
    public List<Etablissement> fetchBagRelationships(List<Etablissement> etablissements) {
        return Optional.of(etablissements).map(this::fetchRessources).get();
    }

    Etablissement fetchRessources(Etablissement result) {
        return entityManager
            .createQuery(
                "select etablissement from Etablissement etablissement left join fetch etablissement.ressources where etablissement is :etablissement",
                Etablissement.class
            )
            .setParameter("etablissement", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Etablissement> fetchRessources(List<Etablissement> etablissements) {
        return entityManager
            .createQuery(
                "select distinct etablissement from Etablissement etablissement left join fetch etablissement.ressources where etablissement in :etablissements",
                Etablissement.class
            )
            .setParameter("etablissements", etablissements)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
