package sn.thiane.mefpai.ent.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.thiane.mefpai.ent.web.rest.TestUtil;

class ProviseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proviseur.class);
        Proviseur proviseur1 = new Proviseur();
        proviseur1.setId(1L);
        Proviseur proviseur2 = new Proviseur();
        proviseur2.setId(proviseur1.getId());
        assertThat(proviseur1).isEqualTo(proviseur2);
        proviseur2.setId(2L);
        assertThat(proviseur1).isNotEqualTo(proviseur2);
        proviseur1.setId(null);
        assertThat(proviseur1).isNotEqualTo(proviseur2);
    }
}
