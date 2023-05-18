package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TypeCharge.
 */
@Entity
@Table(name = "type_charge")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "type")
    @JsonIgnoreProperties(value = { "type" }, allowSetters = true)
    private Set<Charge> charges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeCharge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public TypeCharge nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Charge> getCharges() {
        return this.charges;
    }

    public void setCharges(Set<Charge> charges) {
        if (this.charges != null) {
            this.charges.forEach(i -> i.setType(null));
        }
        if (charges != null) {
            charges.forEach(i -> i.setType(this));
        }
        this.charges = charges;
    }

    public TypeCharge charges(Set<Charge> charges) {
        this.setCharges(charges);
        return this;
    }

    public TypeCharge addCharge(Charge charge) {
        this.charges.add(charge);
        charge.setType(this);
        return this;
    }

    public TypeCharge removeCharge(Charge charge) {
        this.charges.remove(charge);
        charge.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeCharge)) {
            return false;
        }
        return id != null && id.equals(((TypeCharge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeCharge{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
