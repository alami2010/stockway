package com.jbd.stock.service.dto;

import java.util.List;

public class Dashboard {

    List<StatisticsCount> categories;

    List<ArticleDTO> articleOutOfStock;

    public List<StatisticsCount> getCategories() {
        return categories;
    }

    public void setCategories(List<StatisticsCount> categories) {
        this.categories = categories;
    }

    public List<ArticleDTO> getArticleOutOfStock() {
        return articleOutOfStock;
    }

    public void setArticleOutOfStock(List<ArticleDTO> articleOutOfStock) {
        this.articleOutOfStock = articleOutOfStock;
    }
}
