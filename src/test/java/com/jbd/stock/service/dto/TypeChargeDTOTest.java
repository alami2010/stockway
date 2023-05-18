package com.jbd.stock.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeChargeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeChargeDTO.class);
        TypeChargeDTO typeChargeDTO1 = new TypeChargeDTO();
        typeChargeDTO1.setId(1L);
        TypeChargeDTO typeChargeDTO2 = new TypeChargeDTO();
        assertThat(typeChargeDTO1).isNotEqualTo(typeChargeDTO2);
        typeChargeDTO2.setId(typeChargeDTO1.getId());
        assertThat(typeChargeDTO1).isEqualTo(typeChargeDTO2);
        typeChargeDTO2.setId(2L);
        assertThat(typeChargeDTO1).isNotEqualTo(typeChargeDTO2);
        typeChargeDTO1.setId(null);
        assertThat(typeChargeDTO1).isNotEqualTo(typeChargeDTO2);
    }
}
