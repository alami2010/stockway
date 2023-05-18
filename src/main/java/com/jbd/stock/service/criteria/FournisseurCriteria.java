package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Fournisseur} entity. This class is used
 * in {@link com.jbd.stock.web.rest.FournisseurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fournisseurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FournisseurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter ville;

    private StringFilter adresse;

    private StringFilter activite;

    private StringFilter nom;

    private StringFilter description;

    private LongFilter bonReceptionId;

    private LongFilter articleId;

    private Boolean distinct;

    public FournisseurCriteria() {}

    public FournisseurCriteria(FournisseurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.activite = other.activite == null ? null : other.activite.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.bonReceptionId = other.bonReceptionId == null ? null : other.bonReceptionId.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FournisseurCriteria copy() {
        return new FournisseurCriteria(this);
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

    public StringFilter getVille() {
        return ville;
    }

    public StringFilter ville() {
        if (ville == null) {
            ville = new StringFilter();
        }
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public StringFilter adresse() {
        if (adresse == null) {
            adresse = new StringFilter();
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public StringFilter getActivite() {
        return activite;
    }

    public StringFilter activite() {
        if (activite == null) {
            activite = new StringFilter();
        }
        return activite;
    }

    public void setActivite(StringFilter activite) {
        this.activite = activite;
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

    public LongFilter getBonReceptionId() {
        return bonReceptionId;
    }

    public LongFilter bonReceptionId() {
        if (bonReceptionId == null) {
            bonReceptionId = new LongFilter();
        }
        return bonReceptionId;
    }

    public void setBonReceptionId(LongFilter bonReceptionId) {
        this.bonReceptionId = bonReceptionId;
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
        final FournisseurCriteria that = (FournisseurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(activite, that.activite) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(bonReceptionId, that.bonReceptionId) &&
            Objects.equals(articleId, that.articleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, ville, adresse, activite, nom, description, bonReceptionId, articleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FournisseurCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (ville != null ? "ville=" + ville + ", " : "") +
            (adresse != null ? "adresse=" + adresse + ", " : "") +
            (activite != null ? "activite=" + activite + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (bonReceptionId != null ? "bonReceptionId=" + bonReceptionId + ", " : "") +
            (articleId != null ? "articleId=" + articleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
