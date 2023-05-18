package com.jbd.stock.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.BonReceptionItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReceptionItemDTO implements Serializable {

    private Long id;

    private Double qte;

    private BonReceptionDTO bon;

    private ArticleDTO article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQte() {
        return qte;
    }

    public void setQte(Double qte) {
        this.qte = qte;
    }

    public BonReceptionDTO getBon() {
        return bon;
    }

    public void setBon(BonReceptionDTO bon) {
        this.bon = bon;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonReceptionItemDTO)) {
            return false;
        }

        BonReceptionItemDTO bonReceptionItemDTO = (BonReceptionItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bonReceptionItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReceptionItemDTO{" +
            "id=" + getId() +
            ", qte=" + getQte() +
            ", bon=" + getBon() +
            ", article=" + getArticle() +
            "}";
    }
}
