package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Seance;

/**
 * Spring Data SQL repository for the Seance entity.
 */
@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    default Optional<Seance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Seance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Seance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct seance from Seance seance left join fetch seance.contenu left join fetch seance.cours left join fetch seance.salle left join fetch seance.groupe",
        countQuery = "select count(distinct seance) from Seance seance"
    )
    Page<Seance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct seance from Seance seance left join fetch seance.contenu left join fetch seance.cours left join fetch seance.salle left join fetch seance.groupe"
    )
    List<Seance> findAllWithToOneRelationships();

    @Query(
        "select seance from Seance seance left join fetch seance.contenu left join fetch seance.cours left join fetch seance.salle left join fetch seance.groupe where seance.id =:id"
    )
    Optional<Seance> findOneWithToOneRelationships(@Param("id") Long id);
}
