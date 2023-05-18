package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.Category;
import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.dto.CategoryDTO;
import com.jbd.stock.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    ArticleDTO toDto(Article s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);
}
