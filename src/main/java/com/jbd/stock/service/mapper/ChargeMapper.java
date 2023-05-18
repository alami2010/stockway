package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Charge;
import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.service.dto.ChargeDTO;
import com.jbd.stock.service.dto.TypeChargeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Charge} and its DTO {@link ChargeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChargeMapper extends EntityMapper<ChargeDTO, Charge> {
    @Mapping(target = "type", source = "type", qualifiedByName = "typeChargeId")
    ChargeDTO toDto(Charge s);

    @Named("typeChargeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeChargeDTO toDtoTypeChargeId(TypeCharge typeCharge);
}
