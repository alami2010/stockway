package com.jbd.stock.service.criteria;

import com.jbd.stock.domain.enumeration.UserType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Utilisateur} entity. This class is used
 * in {@link com.jbd.stock.web.rest.UtilisateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /utilisateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserType
     */
    public static class UserTypeFilter extends Filter<UserType> {

        public UserTypeFilter() {}

        public UserTypeFilter(UserTypeFilter filter) {
            super(filter);
        }

        @Override
        public UserTypeFilter copy() {
            return new UserTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter nom;

    private StringFilter prenom;

    private LocalDateFilter dateCreation;

    private StringFilter status;

    private StringFilter phone;

    private StringFilter email;

    private StringFilter information;

    private UserTypeFilter type;

    private LongFilter userId;

    private LongFilter paiementId;

    private LongFilter orderId;

    private LongFilter rolesId;

    private Boolean distinct;

    public UtilisateurCriteria() {}

    public UtilisateurCriteria(UtilisateurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.information = other.information == null ? null : other.information.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.paiementId = other.paiementId == null ? null : other.paiementId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.rolesId = other.rolesId == null ? null : other.rolesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UtilisateurCriteria copy() {
        return new UtilisateurCriteria(this);
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

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getInformation() {
        return information;
    }

    public StringFilter information() {
        if (information == null) {
            information = new StringFilter();
        }
        return information;
    }

    public void setInformation(StringFilter information) {
        this.information = information;
    }

    public UserTypeFilter getType() {
        return type;
    }

    public UserTypeFilter type() {
        if (type == null) {
            type = new UserTypeFilter();
        }
        return type;
    }

    public void setType(UserTypeFilter type) {
        this.type = type;
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

    public LongFilter getPaiementId() {
        return paiementId;
    }

    public LongFilter paiementId() {
        if (paiementId == null) {
            paiementId = new LongFilter();
        }
        return paiementId;
    }

    public void setPaiementId(LongFilter paiementId) {
        this.paiementId = paiementId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getRolesId() {
        return rolesId;
    }

    public LongFilter rolesId() {
        if (rolesId == null) {
            rolesId = new LongFilter();
        }
        return rolesId;
    }

    public void setRolesId(LongFilter rolesId) {
        this.rolesId = rolesId;
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
        final UtilisateurCriteria that = (UtilisateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(status, that.status) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(information, that.information) &&
            Objects.equals(type, that.type) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(paiementId, that.paiementId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(rolesId, that.rolesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            nom,
            prenom,
            dateCreation,
            status,
            phone,
            email,
            information,
            type,
            userId,
            paiementId,
            orderId,
            rolesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (information != null ? "information=" + information + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (paiementId != null ? "paiementId=" + paiementId + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (rolesId != null ? "rolesId=" + rolesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
