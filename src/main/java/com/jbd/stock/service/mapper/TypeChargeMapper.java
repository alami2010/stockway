package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.service.dto.TypeChargeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeCharge} and its DTO {@link TypeChargeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeChargeMapper extends EntityMapper<TypeChargeDTO, TypeCharge> {}
