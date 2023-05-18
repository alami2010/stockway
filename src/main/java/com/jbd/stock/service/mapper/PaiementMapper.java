package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Paiement;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.service.dto.PaiementDTO;
import com.jbd.stock.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "user", source = "user", qualifiedByName = "utilisateurId")
    PaiementDTO toDto(Paiement s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
