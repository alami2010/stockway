package com.jbd.stock.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.BonReception} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReceptionDTO implements Serializable {

    private Long id;

    private String informaton;

    private Integer numFacture;

    private Integer numBl;

    private LocalDate dateCreation;

    private FournisseurDTO fournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformaton() {
        return informaton;
    }

    public void setInformaton(String informaton) {
        this.informaton = informaton;
    }

    public Integer getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(Integer numFacture) {
        this.numFacture = numFacture;
    }

    public Integer getNumBl() {
        return numBl;
    }

    public void setNumBl(Integer numBl) {
        this.numBl = numBl;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonReceptionDTO)) {
            return false;
        }

        BonReceptionDTO bonReceptionDTO = (BonReceptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bonReceptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReceptionDTO{" +
            "id=" + getId() +
            ", informaton='" + getInformaton() + "'" +
            ", numFacture=" + getNumFacture() +
            ", numBl=" + getNumBl() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", fournisseur=" + getFournisseur() +
            "}";
    }
}
