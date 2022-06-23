package sn.thiane.mefpai.ent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.thiane.mefpai.ent.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
