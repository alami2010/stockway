package com.jbd.stock.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BonReceptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonReceptionDTO.class);
        BonReceptionDTO bonReceptionDTO1 = new BonReceptionDTO();
        bonReceptionDTO1.setId(1L);
        BonReceptionDTO bonReceptionDTO2 = new BonReceptionDTO();
        assertThat(bonReceptionDTO1).isNotEqualTo(bonReceptionDTO2);
        bonReceptionDTO2.setId(bonReceptionDTO1.getId());
        assertThat(bonReceptionDTO1).isEqualTo(bonReceptionDTO2);
        bonReceptionDTO2.setId(2L);
        assertThat(bonReceptionDTO1).isNotEqualTo(bonReceptionDTO2);
        bonReceptionDTO1.setId(null);
        assertThat(bonReceptionDTO1).isNotEqualTo(bonReceptionDTO2);
    }
}
