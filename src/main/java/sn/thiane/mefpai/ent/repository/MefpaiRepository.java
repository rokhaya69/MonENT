package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Mefpai;

/**
 * Spring Data SQL repository for the Mefpai entity.
 */
@Repository
public interface MefpaiRepository extends JpaRepository<Mefpai, Long> {
    default Optional<Mefpai> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Mefpai> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Mefpai> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct mefpai from Mefpai mefpai left join fetch mefpai.user",
        countQuery = "select count(distinct mefpai) from Mefpai mefpai"
    )
    Page<Mefpai> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct mefpai from Mefpai mefpai left join fetch mefpai.user")
    List<Mefpai> findAllWithToOneRelationships();

    @Query("select mefpai from Mefpai mefpai left join fetch mefpai.user where mefpai.id =:id")
    Optional<Mefpai> findOneWithToOneRelationships(@Param("id") Long id);
}
