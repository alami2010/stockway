package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Role;
import com.jbd.stock.domain.User;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.service.dto.RoleDTO;
import com.jbd.stock.service.dto.UserDTO;
import com.jbd.stock.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "removeRoles", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> role) {
        return role.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }
}
