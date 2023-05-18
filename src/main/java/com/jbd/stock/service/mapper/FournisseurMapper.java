package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {}
