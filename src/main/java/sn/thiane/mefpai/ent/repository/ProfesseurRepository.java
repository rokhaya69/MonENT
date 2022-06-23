package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Professeur;

/**
 * Spring Data SQL repository for the Professeur entity.
 */
@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    default Optional<Professeur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Professeur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Professeur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.etablissement",
        countQuery = "select count(distinct professeur) from Professeur professeur"
    )
    Page<Professeur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.etablissement")
    List<Professeur> findAllWithToOneRelationships();

    @Query(
        "select professeur from Professeur professeur left join fetch professeur.user left join fetch professeur.etablissement where professeur.id =:id"
    )
    Optional<Professeur> findOneWithToOneRelationships(@Param("id") Long id);
}
