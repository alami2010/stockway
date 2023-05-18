package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.BonReception;
import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.service.dto.BonReceptionDTO;
import com.jbd.stock.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BonReception} and its DTO {@link BonReceptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BonReceptionMapper extends EntityMapper<BonReceptionDTO, BonReception> {
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    BonReceptionDTO toDto(BonReception s);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);
}
