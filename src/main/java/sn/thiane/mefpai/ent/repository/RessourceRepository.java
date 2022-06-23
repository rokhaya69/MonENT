package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Ressource;

/**
 * Spring Data SQL repository for the Ressource entity.
 */
@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    default Optional<Ressource> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ressource> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ressource> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ressource from Ressource ressource left join fetch ressource.apprenant left join fetch ressource.groupe left join fetch ressource.cours left join fetch ressource.persoAdmin left join fetch ressource.proviseur left join fetch ressource.directeur left join fetch ressource.inspecteur",
        countQuery = "select count(distinct ressource) from Ressource ressource"
    )
    Page<Ressource> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct ressource from Ressource ressource left join fetch ressource.apprenant left join fetch ressource.groupe left join fetch ressource.cours left join fetch ressource.persoAdmin left join fetch ressource.proviseur left join fetch ressource.directeur left join fetch ressource.inspecteur"
    )
    List<Ressource> findAllWithToOneRelationships();

    @Query(
        "select ressource from Ressource ressource left join fetch ressource.apprenant left join fetch ressource.groupe left join fetch ressource.cours left join fetch ressource.persoAdmin left join fetch ressource.proviseur left join fetch ressource.directeur left join fetch ressource.inspecteur where ressource.id =:id"
    )
    Optional<Ressource> findOneWithToOneRelationships(@Param("id") Long id);
}
