package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Classe;

/**
 * Spring Data SQL repository for the Classe entity.
 */
@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    default Optional<Classe> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Classe> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Classe> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct classe from Classe classe left join fetch classe.filiere left join fetch classe.serie left join fetch classe.proviseur left join fetch classe.directeur left join fetch classe.surveillant left join fetch classe.professeur",
        countQuery = "select count(distinct classe) from Classe classe"
    )
    Page<Classe> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct classe from Classe classe left join fetch classe.filiere left join fetch classe.serie left join fetch classe.proviseur left join fetch classe.directeur left join fetch classe.surveillant left join fetch classe.professeur"
    )
    List<Classe> findAllWithToOneRelationships();

    @Query(
        "select classe from Classe classe left join fetch classe.filiere left join fetch classe.serie left join fetch classe.proviseur left join fetch classe.directeur left join fetch classe.surveillant left join fetch classe.professeur where classe.id =:id"
    )
    Optional<Classe> findOneWithToOneRelationships(@Param("id") Long id);
}
