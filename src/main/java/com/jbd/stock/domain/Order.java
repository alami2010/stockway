package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jbd.stock.domain.enumeration.PaymentStatus;
import com.jbd.stock.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_contact")
    private String clientContact;

    @Column(name = "sub_total")
    private Double subTotal;

    @Column(name = "vat")
    private String vat;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "grand_total")
    private String grandTotal;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "due")
    private String due;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "article", "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "paiements", "orders", "roles" }, allowSetters = true)
    private Utilisateur user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Order date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Order clientName(String clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact() {
        return this.clientContact;
    }

    public Order clientContact(String clientContact) {
        this.setClientContact(clientContact);
        return this;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public Order subTotal(Double subTotal) {
        this.setSubTotal(subTotal);
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getVat() {
        return this.vat;
    }

    public Order vat(String vat) {
        this.setVat(vat);
        return this;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public Order totalAmount(Double totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Order discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getGrandTotal() {
        return this.grandTotal;
    }

    public Order grandTotal(String grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Order paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getDue() {
        return this.due;
    }

    public Order due(String due) {
        this.setDue(due);
        return this;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public Order paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Order paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
    }

    public Order orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public Order addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public Utilisateur getUser() {
        return this.user;
    }

    public void setUser(Utilisateur utilisateur) {
        this.user = utilisateur;
    }

    public Order user(Utilisateur utilisateur) {
        this.setUser(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", clientName='" + getClientName() + "'" +
            ", clientContact='" + getClientContact() + "'" +
            ", subTotal=" + getSubTotal() +
            ", vat='" + getVat() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", discount=" + getDiscount() +
            ", grandTotal='" + getGrandTotal() + "'" +
            ", paid='" + getPaid() + "'" +
            ", due='" + getDue() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
