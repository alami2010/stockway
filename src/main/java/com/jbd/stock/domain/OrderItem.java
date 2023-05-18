package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jbd.stock.domain.enumeration.OrderStatus;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "rate")
    private String rate;

    @Column(name = "prix_vente")
    private Double prixVente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderItems", "bonReceptionItems", "category", "fournisseur" }, allowSetters = true)
    private Article article;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderItems", "user" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(String quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return this.rate;
    }

    public OrderItem rate(String rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Double getPrixVente() {
        return this.prixVente;
    }

    public OrderItem prixVente(Double prixVente) {
        this.setPrixVente(prixVente);
        return this;
    }

    public void setPrixVente(Double prixVente) {
        this.prixVente = prixVente;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public OrderItem status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public OrderItem article(Article article) {
        this.setArticle(article);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity='" + getQuantity() + "'" +
            ", rate='" + getRate() + "'" +
            ", prixVente=" + getPrixVente() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
