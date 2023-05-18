package com.jbd.stock.service.criteria;

import com.jbd.stock.domain.enumeration.PaymentStatus;
import com.jbd.stock.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.Order} entity. This class is used
 * in {@link com.jbd.stock.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentType
     */
    public static class PaymentTypeFilter extends Filter<PaymentType> {

        public PaymentTypeFilter() {}

        public PaymentTypeFilter(PaymentTypeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentTypeFilter copy() {
            return new PaymentTypeFilter(this);
        }
    }

    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {

        public PaymentStatusFilter() {}

        public PaymentStatusFilter(PaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaymentStatusFilter copy() {
            return new PaymentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private StringFilter clientName;

    private StringFilter clientContact;

    private DoubleFilter subTotal;

    private StringFilter vat;

    private DoubleFilter totalAmount;

    private DoubleFilter discount;

    private StringFilter grandTotal;

    private BooleanFilter paid;

    private StringFilter due;

    private PaymentTypeFilter paymentType;

    private PaymentStatusFilter paymentStatus;

    private StringFilter status;

    private LongFilter orderItemId;

    private LongFilter userId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.clientName = other.clientName == null ? null : other.clientName.copy();
        this.clientContact = other.clientContact == null ? null : other.clientContact.copy();
        this.subTotal = other.subTotal == null ? null : other.subTotal.copy();
        this.vat = other.vat == null ? null : other.vat.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.grandTotal = other.grandTotal == null ? null : other.grandTotal.copy();
        this.paid = other.paid == null ? null : other.paid.copy();
        this.due = other.due == null ? null : other.due.copy();
        this.paymentType = other.paymentType == null ? null : other.paymentType.copy();
        this.paymentStatus = other.paymentStatus == null ? null : other.paymentStatus.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getClientName() {
        return clientName;
    }

    public StringFilter clientName() {
        if (clientName == null) {
            clientName = new StringFilter();
        }
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    public StringFilter getClientContact() {
        return clientContact;
    }

    public StringFilter clientContact() {
        if (clientContact == null) {
            clientContact = new StringFilter();
        }
        return clientContact;
    }

    public void setClientContact(StringFilter clientContact) {
        this.clientContact = clientContact;
    }

    public DoubleFilter getSubTotal() {
        return subTotal;
    }

    public DoubleFilter subTotal() {
        if (subTotal == null) {
            subTotal = new DoubleFilter();
        }
        return subTotal;
    }

    public void setSubTotal(DoubleFilter subTotal) {
        this.subTotal = subTotal;
    }

    public StringFilter getVat() {
        return vat;
    }

    public StringFilter vat() {
        if (vat == null) {
            vat = new StringFilter();
        }
        return vat;
    }

    public void setVat(StringFilter vat) {
        this.vat = vat;
    }

    public DoubleFilter getTotalAmount() {
        return totalAmount;
    }

    public DoubleFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new DoubleFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(DoubleFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DoubleFilter getDiscount() {
        return discount;
    }

    public DoubleFilter discount() {
        if (discount == null) {
            discount = new DoubleFilter();
        }
        return discount;
    }

    public void setDiscount(DoubleFilter discount) {
        this.discount = discount;
    }

    public StringFilter getGrandTotal() {
        return grandTotal;
    }

    public StringFilter grandTotal() {
        if (grandTotal == null) {
            grandTotal = new StringFilter();
        }
        return grandTotal;
    }

    public void setGrandTotal(StringFilter grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public BooleanFilter paid() {
        if (paid == null) {
            paid = new BooleanFilter();
        }
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public StringFilter getDue() {
        return due;
    }

    public StringFilter due() {
        if (due == null) {
            due = new StringFilter();
        }
        return due;
    }

    public void setDue(StringFilter due) {
        this.due = due;
    }

    public PaymentTypeFilter getPaymentType() {
        return paymentType;
    }

    public PaymentTypeFilter paymentType() {
        if (paymentType == null) {
            paymentType = new PaymentTypeFilter();
        }
        return paymentType;
    }

    public void setPaymentType(PaymentTypeFilter paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentStatusFilter paymentStatus() {
        if (paymentStatus == null) {
            paymentStatus = new PaymentStatusFilter();
        }
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            orderItemId = new LongFilter();
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(clientName, that.clientName) &&
            Objects.equals(clientContact, that.clientContact) &&
            Objects.equals(subTotal, that.subTotal) &&
            Objects.equals(vat, that.vat) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(grandTotal, that.grandTotal) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(due, that.due) &&
            Objects.equals(paymentType, that.paymentType) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(status, that.status) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            date,
            clientName,
            clientContact,
            subTotal,
            vat,
            totalAmount,
            discount,
            grandTotal,
            paid,
            due,
            paymentType,
            paymentStatus,
            status,
            orderItemId,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (clientName != null ? "clientName=" + clientName + ", " : "") +
            (clientContact != null ? "clientContact=" + clientContact + ", " : "") +
            (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
            (vat != null ? "vat=" + vat + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (grandTotal != null ? "grandTotal=" + grandTotal + ", " : "") +
            (paid != null ? "paid=" + paid + ", " : "") +
            (due != null ? "due=" + due + ", " : "") +
            (paymentType != null ? "paymentType=" + paymentType + ", " : "") +
            (paymentStatus != null ? "paymentStatus=" + paymentStatus + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
