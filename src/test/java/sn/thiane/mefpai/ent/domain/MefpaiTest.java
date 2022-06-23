package sn.thiane.mefpai.ent.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.thiane.mefpai.ent.web.rest.TestUtil;

class MefpaiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mefpai.class);
        Mefpai mefpai1 = new Mefpai();
        mefpai1.setId(1L);
        Mefpai mefpai2 = new Mefpai();
        mefpai2.setId(mefpai1.getId());
        assertThat(mefpai1).isEqualTo(mefpai2);
        mefpai2.setId(2L);
        assertThat(mefpai1).isNotEqualTo(mefpai2);
        mefpai1.setId(null);
        assertThat(mefpai1).isNotEqualTo(mefpai2);
    }
}
