package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A BonReception.
 */
@Entity
@Table(name = "bon_reception")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReception implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "informaton")
    private String informaton;

    @Column(name = "num_facture")
    private Integer numFacture;

    @Column(name = "num_bl")
    private Integer numBl;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "bon")
    @JsonIgnoreProperties(value = { "bon", "article" }, allowSetters = true)
    private Set<BonReceptionItem> bonReceptionItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bonReceptions", "articles" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BonReception id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformaton() {
        return this.informaton;
    }

    public BonReception informaton(String informaton) {
        this.setInformaton(informaton);
        return this;
    }

    public void setInformaton(String informaton) {
        this.informaton = informaton;
    }

    public Integer getNumFacture() {
        return this.numFacture;
    }

    public BonReception numFacture(Integer numFacture) {
        this.setNumFacture(numFacture);
        return this;
    }

    public void setNumFacture(Integer numFacture) {
        this.numFacture = numFacture;
    }

    public Integer getNumBl() {
        return this.numBl;
    }

    public BonReception numBl(Integer numBl) {
        this.setNumBl(numBl);
        return this;
    }

    public void setNumBl(Integer numBl) {
        this.numBl = numBl;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public BonReception dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<BonReceptionItem> getBonReceptionItems() {
        return this.bonReceptionItems;
    }

    public void setBonReceptionItems(Set<BonReceptionItem> bonReceptionItems) {
        if (this.bonReceptionItems != null) {
            this.bonReceptionItems.forEach(i -> i.setBon(null));
        }
        if (bonReceptionItems != null) {
            bonReceptionItems.forEach(i -> i.setBon(this));
        }
        this.bonReceptionItems = bonReceptionItems;
    }

    public BonReception bonReceptionItems(Set<BonReceptionItem> bonReceptionItems) {
        this.setBonReceptionItems(bonReceptionItems);
        return this;
    }

    public BonReception addBonReceptionItem(BonReceptionItem bonReceptionItem) {
        this.bonReceptionItems.add(bonReceptionItem);
        bonReceptionItem.setBon(this);
        return this;
    }

    public BonReception removeBonReceptionItem(BonReceptionItem bonReceptionItem) {
        this.bonReceptionItems.remove(bonReceptionItem);
        bonReceptionItem.setBon(null);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public BonReception fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonReception)) {
            return false;
        }
        return id != null && id.equals(((BonReception) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReception{" +
            "id=" + getId() +
            ", informaton='" + getInformaton() + "'" +
            ", numFacture=" + getNumFacture() +
            ", numBl=" + getNumBl() +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
