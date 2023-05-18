package com.jbd.stock.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbd.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BonReceptionItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonReceptionItemDTO.class);
        BonReceptionItemDTO bonReceptionItemDTO1 = new BonReceptionItemDTO();
        bonReceptionItemDTO1.setId(1L);
        BonReceptionItemDTO bonReceptionItemDTO2 = new BonReceptionItemDTO();
        assertThat(bonReceptionItemDTO1).isNotEqualTo(bonReceptionItemDTO2);
        bonReceptionItemDTO2.setId(bonReceptionItemDTO1.getId());
        assertThat(bonReceptionItemDTO1).isEqualTo(bonReceptionItemDTO2);
        bonReceptionItemDTO2.setId(2L);
        assertThat(bonReceptionItemDTO1).isNotEqualTo(bonReceptionItemDTO2);
        bonReceptionItemDTO1.setId(null);
        assertThat(bonReceptionItemDTO1).isNotEqualTo(bonReceptionItemDTO2);
    }
}
