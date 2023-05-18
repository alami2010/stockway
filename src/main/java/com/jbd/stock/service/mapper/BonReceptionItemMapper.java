package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.BonReception;
import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.dto.BonReceptionDTO;
import com.jbd.stock.service.dto.BonReceptionItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BonReceptionItem} and its DTO {@link BonReceptionItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BonReceptionItemMapper extends EntityMapper<BonReceptionItemDTO, BonReceptionItem> {
    @Mapping(target = "bon", source = "bon", qualifiedByName = "bonReceptionId")
    @Mapping(target = "article", source = "article", qualifiedByName = "articleId")
    BonReceptionItemDTO toDto(BonReceptionItem s);

    @Named("bonReceptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BonReceptionDTO toDtoBonReceptionId(BonReception bonReception);

    @Named("articleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleDTO toDtoArticleId(Article article);
}
