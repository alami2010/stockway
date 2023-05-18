package com.jbd.stock.service.mapper;

import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.Order;
import com.jbd.stock.domain.OrderItem;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.dto.OrderDTO;
import com.jbd.stock.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "article", source = "article", qualifiedByName = "articleId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("articleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleDTO toDtoArticleId(Article article);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
