package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Programme;

/**
 * Spring Data SQL repository for the Programme entity.
 */
@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {
    default Optional<Programme> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Programme> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Programme> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct programme from Programme programme left join fetch programme.filiere left join fetch programme.serie",
        countQuery = "select count(distinct programme) from Programme programme"
    )
    Page<Programme> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct programme from Programme programme left join fetch programme.filiere left join fetch programme.serie")
    List<Programme> findAllWithToOneRelationships();

    @Query(
        "select programme from Programme programme left join fetch programme.filiere left join fetch programme.serie where programme.id =:id"
    )
    Optional<Programme> findOneWithToOneRelationships(@Param("id") Long id);
}
