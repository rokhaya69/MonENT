package sn.thiane.mefpai.ent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.thiane.mefpai.ent.domain.Surveillant;

public interface SurveillantRepositoryWithBagRelationships {
    Optional<Surveillant> fetchBagRelationships(Optional<Surveillant> surveillant);

    List<Surveillant> fetchBagRelationships(List<Surveillant> surveillants);

    Page<Surveillant> fetchBagRelationships(Page<Surveillant> surveillants);
}
