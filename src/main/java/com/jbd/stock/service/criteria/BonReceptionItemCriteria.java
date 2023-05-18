package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.BonReceptionItem} entity. This class is used
 * in {@link com.jbd.stock.web.rest.BonReceptionItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bon-reception-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReceptionItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter qte;

    private LongFilter bonId;

    private LongFilter articleId;

    private Boolean distinct;

    public BonReceptionItemCriteria() {}

    public BonReceptionItemCriteria(BonReceptionItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qte = other.qte == null ? null : other.qte.copy();
        this.bonId = other.bonId == null ? null : other.bonId.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BonReceptionItemCriteria copy() {
        return new BonReceptionItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getQte() {
        return qte;
    }

    public DoubleFilter qte() {
        if (qte == null) {
            qte = new DoubleFilter();
        }
        return qte;
    }

    public void setQte(DoubleFilter qte) {
        this.qte = qte;
    }

    public LongFilter getBonId() {
        return bonId;
    }

    public LongFilter bonId() {
        if (bonId == null) {
            bonId = new LongFilter();
        }
        return bonId;
    }

    public void setBonId(LongFilter bonId) {
        this.bonId = bonId;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public LongFilter articleId() {
        if (articleId == null) {
            articleId = new LongFilter();
        }
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BonReceptionItemCriteria that = (BonReceptionItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(qte, that.qte) &&
            Objects.equals(bonId, that.bonId) &&
            Objects.equals(articleId, that.articleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qte, bonId, articleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReceptionItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qte != null ? "qte=" + qte + ", " : "") +
            (bonId != null ? "bonId=" + bonId + ", " : "") +
            (articleId != null ? "articleId=" + articleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
