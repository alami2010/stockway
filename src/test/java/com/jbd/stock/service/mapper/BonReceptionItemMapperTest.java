package com.jbd.stock.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BonReceptionItemMapperTest {

    private BonReceptionItemMapper bonReceptionItemMapper;

    @BeforeEach
    public void setUp() {
        bonReceptionItemMapper = new BonReceptionItemMapperImpl();
    }
}
