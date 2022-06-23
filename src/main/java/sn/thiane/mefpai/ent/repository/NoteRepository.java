package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Note;

/**
 * Spring Data SQL repository for the Note entity.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    default Optional<Note> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Note> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Note> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct note from Note note left join fetch note.apprenant left join fetch note.evaluation",
        countQuery = "select count(distinct note) from Note note"
    )
    Page<Note> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct note from Note note left join fetch note.apprenant left join fetch note.evaluation")
    List<Note> findAllWithToOneRelationships();

    @Query("select note from Note note left join fetch note.apprenant left join fetch note.evaluation where note.id =:id")
    Optional<Note> findOneWithToOneRelationships(@Param("id") Long id);
}
