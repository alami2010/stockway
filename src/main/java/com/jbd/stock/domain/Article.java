package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "prix_achat")
    private Double prixAchat;

    @Column(name = "qte")
    private Double qte;

    @Column(name = "qte_alert")
    private Double qteAlert;

    @Column(name = "status")
    private String status;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties(value = { "article", "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties(value = { "bon", "article" }, allowSetters = true)
    private Set<BonReceptionItem> bonReceptionItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles" }, allowSetters = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bonReceptions", "articles" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Article code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Article nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Article description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrixAchat() {
        return this.prixAchat;
    }

    public Article prixAchat(Double prixAchat) {
        this.setPrixAchat(prixAchat);
        return this;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Double getQte() {
        return this.qte;
    }

    public Article qte(Double qte) {
        this.setQte(qte);
        return this;
    }

    public void setQte(Double qte) {
        this.qte = qte;
    }

    public Double getQteAlert() {
        return this.qteAlert;
    }

    public Article qteAlert(Double qteAlert) {
        this.setQteAlert(qteAlert);
        return this;
    }

    public void setQteAlert(Double qteAlert) {
        this.qteAlert = qteAlert;
    }

    public String getStatus() {
        return this.status;
    }

    public Article status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Article dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setArticle(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setArticle(this));
        }
        this.orderItems = orderItems;
    }

    public Article orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public Article addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setArticle(this);
        return this;
    }

    public Article removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setArticle(null);
        return this;
    }

    public Set<BonReceptionItem> getBonReceptionItems() {
        return this.bonReceptionItems;
    }

    public void setBonReceptionItems(Set<BonReceptionItem> bonReceptionItems) {
        if (this.bonReceptionItems != null) {
            this.bonReceptionItems.forEach(i -> i.setArticle(null));
        }
        if (bonReceptionItems != null) {
            bonReceptionItems.forEach(i -> i.setArticle(this));
        }
        this.bonReceptionItems = bonReceptionItems;
    }

    public Article bonReceptionItems(Set<BonReceptionItem> bonReceptionItems) {
        this.setBonReceptionItems(bonReceptionItems);
        return this;
    }

    public Article addBonReceptionItem(BonReceptionItem bonReceptionItem) {
        this.bonReceptionItems.add(bonReceptionItem);
        bonReceptionItem.setArticle(this);
        return this;
    }

    public Article removeBonReceptionItem(BonReceptionItem bonReceptionItem) {
        this.bonReceptionItems.remove(bonReceptionItem);
        bonReceptionItem.setArticle(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Article category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Article fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", prixAchat=" + getPrixAchat() +
            ", qte=" + getQte() +
            ", qteAlert=" + getQteAlert() +
            ", status='" + getStatus() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
