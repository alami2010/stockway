package com.jbd.stock.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.TypeCharge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeChargeDTO implements Serializable {

    private Long id;

    private String nom;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeChargeDTO)) {
            return false;
        }

        TypeChargeDTO typeChargeDTO = (TypeChargeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeChargeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeChargeDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
