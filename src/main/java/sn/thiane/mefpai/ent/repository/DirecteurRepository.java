package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Directeur;

/**
 * Spring Data SQL repository for the Directeur entity.
 */
@Repository
public interface DirecteurRepository extends JpaRepository<Directeur, Long> {
    default Optional<Directeur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Directeur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Directeur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct directeur from Directeur directeur left join fetch directeur.user",
        countQuery = "select count(distinct directeur) from Directeur directeur"
    )
    Page<Directeur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct directeur from Directeur directeur left join fetch directeur.user")
    List<Directeur> findAllWithToOneRelationships();

    @Query("select directeur from Directeur directeur left join fetch directeur.user where directeur.id =:id")
    Optional<Directeur> findOneWithToOneRelationships(@Param("id") Long id);
}
