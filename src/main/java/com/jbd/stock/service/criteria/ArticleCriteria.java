package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Article} entity. This class is used
 * in {@link com.jbd.stock.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter nom;

    private StringFilter description;

    private DoubleFilter prixAchat;

    private DoubleFilter qte;

    private DoubleFilter qteAlert;

    private StringFilter status;

    private LocalDateFilter dateCreation;

    private LongFilter orderItemId;

    private LongFilter bonReceptionItemId;

    private LongFilter categoryId;

    private LongFilter fournisseurId;

    private Boolean distinct;

    public ArticleCriteria() {}

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.prixAchat = other.prixAchat == null ? null : other.prixAchat.copy();
        this.qte = other.qte == null ? null : other.qte.copy();
        this.qteAlert = other.qteAlert == null ? null : other.qteAlert.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.bonReceptionItemId = other.bonReceptionItemId == null ? null : other.bonReceptionItemId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.fournisseurId = other.fournisseurId == null ? null : other.fournisseurId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getPrixAchat() {
        return prixAchat;
    }

    public DoubleFilter prixAchat() {
        if (prixAchat == null) {
            prixAchat = new DoubleFilter();
        }
        return prixAchat;
    }

    public void setPrixAchat(DoubleFilter prixAchat) {
        this.prixAchat = prixAchat;
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

    public DoubleFilter getQteAlert() {
        return qteAlert;
    }

    public DoubleFilter qteAlert() {
        if (qteAlert == null) {
            qteAlert = new DoubleFilter();
        }
        return qteAlert;
    }

    public void setQteAlert(DoubleFilter qteAlert) {
        this.qteAlert = qteAlert;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LocalDateFilter getDateCreation() {
        return dateCreation;
    }

    public LocalDateFilter dateCreation() {
        if (dateCreation == null) {
            dateCreation = new LocalDateFilter();
        }
        return dateCreation;
    }

    public void setDateCreation(LocalDateFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            orderItemId = new LongFilter();
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getBonReceptionItemId() {
        return bonReceptionItemId;
    }

    public LongFilter bonReceptionItemId() {
        if (bonReceptionItemId == null) {
            bonReceptionItemId = new LongFilter();
        }
        return bonReceptionItemId;
    }

    public void setBonReceptionItemId(LongFilter bonReceptionItemId) {
        this.bonReceptionItemId = bonReceptionItemId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getFournisseurId() {
        return fournisseurId;
    }

    public LongFilter fournisseurId() {
        if (fournisseurId == null) {
            fournisseurId = new LongFilter();
        }
        return fournisseurId;
    }

    public void setFournisseurId(LongFilter fournisseurId) {
        this.fournisseurId = fournisseurId;
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
        final ArticleCriteria that = (ArticleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(prixAchat, that.prixAchat) &&
            Objects.equals(qte, that.qte) &&
            Objects.equals(qteAlert, that.qteAlert) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(bonReceptionItemId, that.bonReceptionItemId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(fournisseurId, that.fournisseurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            nom,
            description,
            prixAchat,
            qte,
            qteAlert,
            status,
            dateCreation,
            orderItemId,
            bonReceptionItemId,
            categoryId,
            fournisseurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (prixAchat != null ? "prixAchat=" + prixAchat + ", " : "") +
            (qte != null ? "qte=" + qte + ", " : "") +
            (qteAlert != null ? "qteAlert=" + qteAlert + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (bonReceptionItemId != null ? "bonReceptionItemId=" + bonReceptionItemId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (fournisseurId != null ? "fournisseurId=" + fournisseurId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
