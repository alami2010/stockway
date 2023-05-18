package com.jbd.stock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeChargeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeCharge.class);
        TypeCharge typeCharge1 = new TypeCharge();
        typeCharge1.setId(1L);
        TypeCharge typeCharge2 = new TypeCharge();
        typeCharge2.setId(typeCharge1.getId());
        assertThat(typeCharge1).isEqualTo(typeCharge2);
        typeCharge2.setId(2L);
        assertThat(typeCharge1).isNotEqualTo(typeCharge2);
        typeCharge1.setId(null);
        assertThat(typeCharge1).isNotEqualTo(typeCharge2);
    }
}
