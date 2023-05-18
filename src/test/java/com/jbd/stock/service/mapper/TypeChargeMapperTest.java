package com.jbd.stock.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeChargeMapperTest {

    private TypeChargeMapper typeChargeMapper;

    @BeforeEach
    public void setUp() {
        typeChargeMapper = new TypeChargeMapperImpl();
    }
}
