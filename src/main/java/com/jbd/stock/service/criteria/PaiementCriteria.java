package com.jbd.stock.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Paiement} entity. This class is used
 * in {@link com.jbd.stock.web.rest.PaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter remunuration;

    private LocalDateFilter dateCreation;

    private LongFilter userId;

    private Boolean distinct;

    public PaiementCriteria() {}

    public PaiementCriteria(PaiementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remunuration = other.remunuration == null ? null : other.remunuration.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaiementCriteria copy() {
        return new PaiementCriteria(this);
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

    public DoubleFilter getRemunuration() {
        return remunuration;
    }

    public DoubleFilter remunuration() {
        if (remunuration == null) {
            remunuration = new DoubleFilter();
        }
        return remunuration;
    }

    public void setRemunuration(DoubleFilter remunuration) {
        this.remunuration = remunuration;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final PaiementCriteria that = (PaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remunuration, that.remunuration) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remunuration, dateCreation, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remunuration != null ? "remunuration=" + remunuration + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
