package com.jbd.stock.service.dto;

import com.jbd.stock.domain.enumeration.PaymentStatus;
import com.jbd.stock.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.jbd.stock.domain.Order} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private String clientName;

    private String clientContact;

    private Double subTotal;

    private String vat;

    private Double totalAmount;

    private Double discount;

    private String grandTotal;

    private Boolean paid;

    private String due;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private String status;

    private UtilisateurDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UtilisateurDTO getUser() {
        return user;
    }

    public void setUser(UtilisateurDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
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
            ", user=" + getUser() +
            "}";
    }
}
