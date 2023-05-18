package com.jbd.stock.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.Charge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChargeDTO implements Serializable {

    private Long id;

    private String nom;

    private Double valeur;

    private LocalDate dateCreation;

    private TypeChargeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeChargeDTO getType() {
        return type;
    }

    public void setType(TypeChargeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChargeDTO)) {
            return false;
        }

        ChargeDTO chargeDTO = (ChargeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chargeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChargeDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", valeur=" + getValeur() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", type=" + getType() +
            "}";
    }
}
