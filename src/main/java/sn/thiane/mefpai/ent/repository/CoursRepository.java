package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Cours;

/**
 * Spring Data SQL repository for the Cours entity.
 */
@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    default Optional<Cours> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cours> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cours> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct cours from Cours cours left join fetch cours.matiere left join fetch cours.classe left join fetch cours.professeur left join fetch cours.syllabus",
        countQuery = "select count(distinct cours) from Cours cours"
    )
    Page<Cours> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct cours from Cours cours left join fetch cours.matiere left join fetch cours.classe left join fetch cours.professeur left join fetch cours.syllabus"
    )
    List<Cours> findAllWithToOneRelationships();

    @Query(
        "select cours from Cours cours left join fetch cours.matiere left join fetch cours.classe left join fetch cours.professeur left join fetch cours.syllabus where cours.id =:id"
    )
    Optional<Cours> findOneWithToOneRelationships(@Param("id") Long id);
}
