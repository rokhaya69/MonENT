package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Parent;

/**
 * Spring Data SQL repository for the Parent entity.
 */
@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    default Optional<Parent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Parent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Parent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct parent from Parent parent left join fetch parent.user",
        countQuery = "select count(distinct parent) from Parent parent"
    )
    Page<Parent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct parent from Parent parent left join fetch parent.user")
    List<Parent> findAllWithToOneRelationships();

    @Query("select parent from Parent parent left join fetch parent.user where parent.id =:id")
    Optional<Parent> findOneWithToOneRelationships(@Param("id") Long id);
}
