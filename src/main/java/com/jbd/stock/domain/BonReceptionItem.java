package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A BonReceptionItem.
 */
@Entity
@Table(name = "bon_reception_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BonReceptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qte")
    private Double qte;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bonReceptionItems", "fournisseur" }, allowSetters = true)
    private BonReception bon;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderItems", "bonReceptionItems", "category", "fournisseur" }, allowSetters = true)
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BonReceptionItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQte() {
        return this.qte;
    }

    public BonReceptionItem qte(Double qte) {
        this.setQte(qte);
        return this;
    }

    public void setQte(Double qte) {
        this.qte = qte;
    }

    public BonReception getBon() {
        return this.bon;
    }

    public void setBon(BonReception bonReception) {
        this.bon = bonReception;
    }

    public BonReceptionItem bon(BonReception bonReception) {
        this.setBon(bonReception);
        return this;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public BonReceptionItem article(Article article) {
        this.setArticle(article);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonReceptionItem)) {
            return false;
        }
        return id != null && id.equals(((BonReceptionItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonReceptionItem{" +
            "id=" + getId() +
            ", qte=" + getQte() +
            "}";
    }
}
