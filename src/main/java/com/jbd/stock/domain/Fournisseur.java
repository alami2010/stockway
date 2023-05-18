package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "ville")
    private String ville;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "activite")
    private String activite;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "bonReceptionItems", "fournisseur" }, allowSetters = true)
    private Set<BonReception> bonReceptions = new HashSet<>();

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "orderItems", "bonReceptionItems", "category", "fournisseur" }, allowSetters = true)
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fournisseur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Fournisseur code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVille() {
        return this.ville;
    }

    public Fournisseur ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Fournisseur adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getActivite() {
        return this.activite;
    }

    public Fournisseur activite(String activite) {
        this.setActivite(activite);
        return this;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getNom() {
        return this.nom;
    }

    public Fournisseur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Fournisseur description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BonReception> getBonReceptions() {
        return this.bonReceptions;
    }

    public void setBonReceptions(Set<BonReception> bonReceptions) {
        if (this.bonReceptions != null) {
            this.bonReceptions.forEach(i -> i.setFournisseur(null));
        }
        if (bonReceptions != null) {
            bonReceptions.forEach(i -> i.setFournisseur(this));
        }
        this.bonReceptions = bonReceptions;
    }

    public Fournisseur bonReceptions(Set<BonReception> bonReceptions) {
        this.setBonReceptions(bonReceptions);
        return this;
    }

    public Fournisseur addBonReception(BonReception bonReception) {
        this.bonReceptions.add(bonReception);
        bonReception.setFournisseur(this);
        return this;
    }

    public Fournisseur removeBonReception(BonReception bonReception) {
        this.bonReceptions.remove(bonReception);
        bonReception.setFournisseur(null);
        return this;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setFournisseur(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setFournisseur(this));
        }
        this.articles = articles;
    }

    public Fournisseur articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public Fournisseur addArticle(Article article) {
        this.articles.add(article);
        article.setFournisseur(this);
        return this;
    }

    public Fournisseur removeArticle(Article article) {
        this.articles.remove(article);
        article.setFournisseur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ville='" + getVille() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", activite='" + getActivite() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
