package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Inspecteur;

/**
 * Spring Data SQL repository for the Inspecteur entity.
 */
@Repository
public interface InspecteurRepository extends JpaRepository<Inspecteur, Long> {
    default Optional<Inspecteur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inspecteur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inspecteur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inspecteur from Inspecteur inspecteur left join fetch inspecteur.user",
        countQuery = "select count(distinct inspecteur) from Inspecteur inspecteur"
    )
    Page<Inspecteur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct inspecteur from Inspecteur inspecteur left join fetch inspecteur.user")
    List<Inspecteur> findAllWithToOneRelationships();

    @Query("select inspecteur from Inspecteur inspecteur left join fetch inspecteur.user where inspecteur.id =:id")
    Optional<Inspecteur> findOneWithToOneRelationships(@Param("id") Long id);
}
