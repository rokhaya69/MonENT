package sn.thiane.mefpai.ent.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.thiane.mefpai.ent.web.rest.TestUtil;

class ParentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parent.class);
        Parent parent1 = new Parent();
        parent1.setId(1L);
        Parent parent2 = new Parent();
        parent2.setId(parent1.getId());
        assertThat(parent1).isEqualTo(parent2);
        parent2.setId(2L);
        assertThat(parent1).isNotEqualTo(parent2);
        parent1.setId(null);
        assertThat(parent1).isNotEqualTo(parent2);
    }
}
