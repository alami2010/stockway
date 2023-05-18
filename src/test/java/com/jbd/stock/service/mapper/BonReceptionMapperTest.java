package com.jbd.stock.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BonReceptionMapperTest {

    private BonReceptionMapper bonReceptionMapper;

    @BeforeEach
    public void setUp() {
        bonReceptionMapper = new BonReceptionMapperImpl();
    }
}
