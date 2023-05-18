package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.BonReception} entity. This class is used
 * in {@link com.jbd.stock.web.rest.BonReceptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bon-receptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReceptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter informaton;

    private IntegerFilter numFacture;

    private IntegerFilter numBl;

    private LocalDateFilter dateCreation;

    private LongFilter bonReceptionItemId;

    private LongFilter fournisseurId;

    private Boolean distinct;

    public BonReceptionCriteria() {}

    public BonReceptionCriteria(BonReceptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.informaton = other.informaton == null ? null : other.informaton.copy();
        this.numFacture = other.numFacture == null ? null : other.numFacture.copy();
        this.numBl = other.numBl == null ? null : other.numBl.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.bonReceptionItemId = other.bonReceptionItemId == null ? null : other.bonReceptionItemId.copy();
        this.fournisseurId = other.fournisseurId == null ? null : other.fournisseurId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BonReceptionCriteria copy() {
        return new BonReceptionCriteria(this);
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

    public StringFilter getInformaton() {
        return informaton;
    }

    public StringFilter informaton() {
        if (informaton == null) {
            informaton = new StringFilter();
        }
        return informaton;
    }

    public void setInformaton(StringFilter informaton) {
        this.informaton = informaton;
    }

    public IntegerFilter getNumFacture() {
        return numFacture;
    }

    public IntegerFilter numFacture() {
        if (numFacture == null) {
            numFacture = new IntegerFilter();
        }
        return numFacture;
    }

    public void setNumFacture(IntegerFilter numFacture) {
        this.numFacture = numFacture;
    }

    public IntegerFilter getNumBl() {
        return numBl;
    }

    public IntegerFilter numBl() {
        if (numBl == null) {
            numBl = new IntegerFilter();
        }
        return numBl;
    }

    public void setNumBl(IntegerFilter numBl) {
        this.numBl = numBl;
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
        final BonReceptionCriteria that = (BonReceptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(informaton, that.informaton) &&
            Objects.equals(numFacture, that.numFacture) &&
            Objects.equals(numBl, that.numBl) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(bonReceptionItemId, that.bonReceptionItemId) &&
            Objects.equals(fournisseurId, that.fournisseurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, informaton, numFacture, numBl, dateCreation, bonReceptionItemId, fournisseurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReceptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (informaton != null ? "informaton=" + informaton + ", " : "") +
            (numFacture != null ? "numFacture=" + numFacture + ", " : "") +
            (numBl != null ? "numBl=" + numBl + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (bonReceptionItemId != null ? "bonReceptionItemId=" + bonReceptionItemId + ", " : "") +
            (fournisseurId != null ? "fournisseurId=" + fournisseurId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
