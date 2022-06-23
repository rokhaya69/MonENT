package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.thiane.mefpai.ent.domain.Surveillant;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SurveillantRepositoryWithBagRelationshipsImpl implements SurveillantRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Surveillant> fetchBagRelationships(Optional<Surveillant> surveillant) {
        return surveillant.map(this::fetchEvaluations);
    }

    @Override
    public Page<Surveillant> fetchBagRelationships(Page<Surveillant> surveillants) {
        return new PageImpl<>(
            fetchBagRelationships(surveillants.getContent()),
            surveillants.getPageable(),
            surveillants.getTotalElements()
        );
    }

    @Override
    public List<Surveillant> fetchBagRelationships(List<Surveillant> surveillants) {
        return Optional.of(surveillants).map(this::fetchEvaluations).get();
    }

    Surveillant fetchEvaluations(Surveillant result) {
        return entityManager
            .createQuery(
                "select surveillant from Surveillant surveillant left join fetch surveillant.evaluations where surveillant is :surveillant",
                Surveillant.class
            )
            .setParameter("surveillant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Surveillant> fetchEvaluations(List<Surveillant> surveillants) {
        return entityManager
            .createQuery(
                "select distinct surveillant from Surveillant surveillant left join fetch surveillant.evaluations where surveillant in :surveillants",
                Surveillant.class
            )
            .setParameter("surveillants", surveillants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
