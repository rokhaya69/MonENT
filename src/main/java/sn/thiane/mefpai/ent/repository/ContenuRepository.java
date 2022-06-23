package sn.thiane.mefpai.ent.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.thiane.mefpai.ent.domain.Contenu;

/**
 * Spring Data SQL repository for the Contenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContenuRepository extends JpaRepository<Contenu, Long> {}
