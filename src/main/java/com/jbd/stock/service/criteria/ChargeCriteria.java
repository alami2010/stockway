package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Charge} entity. This class is used
 * in {@link com.jbd.stock.web.rest.ChargeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /charges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChargeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private DoubleFilter valeur;

    private LocalDateFilter dateCreation;

    private LongFilter typeId;

    private Boolean distinct;

    public ChargeCriteria() {}

    public ChargeCriteria(ChargeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.valeur = other.valeur == null ? null : other.valeur.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ChargeCriteria copy() {
        return new ChargeCriteria(this);
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

    public DoubleFilter getValeur() {
        return valeur;
    }

    public DoubleFilter valeur() {
        if (valeur == null) {
            valeur = new DoubleFilter();
        }
        return valeur;
    }

    public void setValeur(DoubleFilter valeur) {
        this.valeur = valeur;
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

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
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
        final ChargeCriteria that = (ChargeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(valeur, that.valeur) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, valeur, dateCreation, typeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChargeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (valeur != null ? "valeur=" + valeur + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
