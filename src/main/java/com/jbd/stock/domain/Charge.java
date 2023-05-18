package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Charge.
 */
@Entity
@Table(name = "charge")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Charge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "valeur")
    private Double valeur;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "charges" }, allowSetters = true)
    private TypeCharge type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Charge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Charge nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getValeur() {
        return this.valeur;
    }

    public Charge valeur(Double valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Charge dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeCharge getType() {
        return this.type;
    }

    public void setType(TypeCharge typeCharge) {
        this.type = typeCharge;
    }

    public Charge type(TypeCharge typeCharge) {
        this.setType(typeCharge);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Charge)) {
            return false;
        }
        return id != null && id.equals(((Charge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Charge{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", valeur=" + getValeur() +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
