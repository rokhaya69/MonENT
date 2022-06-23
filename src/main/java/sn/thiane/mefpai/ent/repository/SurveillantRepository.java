package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Surveillant;

/**
 * Spring Data SQL repository for the Surveillant entity.
 */
@Repository
public interface SurveillantRepository extends SurveillantRepositoryWithBagRelationships, JpaRepository<Surveillant, Long> {
    default Optional<Surveillant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Surveillant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Surveillant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct surveillant from Surveillant surveillant left join fetch surveillant.user",
        countQuery = "select count(distinct surveillant) from Surveillant surveillant"
    )
    Page<Surveillant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct surveillant from Surveillant surveillant left join fetch surveillant.user")
    List<Surveillant> findAllWithToOneRelationships();

    @Query("select surveillant from Surveillant surveillant left join fetch surveillant.user where surveillant.id =:id")
    Optional<Surveillant> findOneWithToOneRelationships(@Param("id") Long id);
}
