package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Evaluation;

/**
 * Spring Data SQL repository for the Evaluation entity.
 */
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    default Optional<Evaluation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Evaluation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Evaluation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct evaluation from Evaluation evaluation left join fetch evaluation.matiere left join fetch evaluation.groupe left join fetch evaluation.professeur left join fetch evaluation.salle",
        countQuery = "select count(distinct evaluation) from Evaluation evaluation"
    )
    Page<Evaluation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct evaluation from Evaluation evaluation left join fetch evaluation.matiere left join fetch evaluation.groupe left join fetch evaluation.professeur left join fetch evaluation.salle"
    )
    List<Evaluation> findAllWithToOneRelationships();

    @Query(
        "select evaluation from Evaluation evaluation left join fetch evaluation.matiere left join fetch evaluation.groupe left join fetch evaluation.professeur left join fetch evaluation.salle where evaluation.id =:id"
    )
    Optional<Evaluation> findOneWithToOneRelationships(@Param("id") Long id);
}
