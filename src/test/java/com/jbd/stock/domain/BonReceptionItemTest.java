package com.jbd.stock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BonReceptionItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonReceptionItem.class);
        BonReceptionItem bonReceptionItem1 = new BonReceptionItem();
        bonReceptionItem1.setId(1L);
        BonReceptionItem bonReceptionItem2 = new BonReceptionItem();
        bonReceptionItem2.setId(bonReceptionItem1.getId());
        assertThat(bonReceptionItem1).isEqualTo(bonReceptionItem2);
        bonReceptionItem2.setId(2L);
        assertThat(bonReceptionItem1).isNotEqualTo(bonReceptionItem2);
        bonReceptionItem1.setId(null);
        assertThat(bonReceptionItem1).isNotEqualTo(bonReceptionItem2);
    }
}
