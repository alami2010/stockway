package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "remunuration")
    private Double remunuration;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "paiements", "orders", "roles" }, allowSetters = true)
    private Utilisateur user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRemunuration() {
        return this.remunuration;
    }

    public Paiement remunuration(Double remunuration) {
        this.setRemunuration(remunuration);
        return this;
    }

    public void setRemunuration(Double remunuration) {
        this.remunuration = remunuration;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Paiement dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getUser() {
        return this.user;
    }

    public void setUser(Utilisateur utilisateur) {
        this.user = utilisateur;
    }

    public Paiement user(Utilisateur utilisateur) {
        this.setUser(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paiement)) {
            return false;
        }
        return id != null && id.equals(((Paiement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + getId() +
            ", remunuration=" + getRemunuration() +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
