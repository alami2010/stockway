package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Order;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.service.dto.OrderDTO;
import com.jbd.stock.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "user", source = "user", qualifiedByName = "utilisateurId")
    OrderDTO toDto(Order s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
