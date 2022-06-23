package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Proviseur;

/**
 * Spring Data SQL repository for the Proviseur entity.
 */
@Repository
public interface ProviseurRepository extends JpaRepository<Proviseur, Long> {
    default Optional<Proviseur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Proviseur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Proviseur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct proviseur from Proviseur proviseur left join fetch proviseur.user",
        countQuery = "select count(distinct proviseur) from Proviseur proviseur"
    )
    Page<Proviseur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct proviseur from Proviseur proviseur left join fetch proviseur.user")
    List<Proviseur> findAllWithToOneRelationships();

    @Query("select proviseur from Proviseur proviseur left join fetch proviseur.user where proviseur.id =:id")
    Optional<Proviseur> findOneWithToOneRelationships(@Param("id") Long id);
}
