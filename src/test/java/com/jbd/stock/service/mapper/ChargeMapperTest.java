package com.jbd.stock.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChargeMapperTest {

    private ChargeMapper chargeMapper;

    @BeforeEach
    public void setUp() {
        chargeMapper = new ChargeMapperImpl();
    }
}
