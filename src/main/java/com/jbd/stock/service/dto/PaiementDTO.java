package com.jbd.stock.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.Paiement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementDTO implements Serializable {

    private Long id;

    private Double remunuration;

    private LocalDate dateCreation;

    private UtilisateurDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRemunuration() {
        return remunuration;
    }

    public void setRemunuration(Double remunuration) {
        this.remunuration = remunuration;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public UtilisateurDTO getUser() {
        return user;
    }

    public void setUser(UtilisateurDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementDTO)) {
            return false;
        }

        PaiementDTO paiementDTO = (PaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + getId() +
            ", remunuration=" + getRemunuration() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
