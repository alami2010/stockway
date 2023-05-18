package com.jbd.stock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChargeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Charge.class);
        Charge charge1 = new Charge();
        charge1.setId(1L);
        Charge charge2 = new Charge();
        charge2.setId(charge1.getId());
        assertThat(charge1).isEqualTo(charge2);
        charge2.setId(2L);
        assertThat(charge1).isNotEqualTo(charge2);
        charge1.setId(null);
        assertThat(charge1).isNotEqualTo(charge2);
    }
}
