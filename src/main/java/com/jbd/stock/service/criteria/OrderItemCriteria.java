package com.jbd.stock.service.criteria;

import com.jbd.stock.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.jbd.stock.domain.OrderItem} entity. This class is used
 * in {@link com.jbd.stock.web.rest.OrderItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter quantity;

    private StringFilter rate;

    private DoubleFilter prixVente;

    private OrderStatusFilter status;

    private LongFilter articleId;

    private LongFilter orderId;

    private Boolean distinct;

    public OrderItemCriteria() {}

    public OrderItemCriteria(OrderItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.rate = other.rate == null ? null : other.rate.copy();
        this.prixVente = other.prixVente == null ? null : other.prixVente.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderItemCriteria copy() {
        return new OrderItemCriteria(this);
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

    public StringFilter getQuantity() {
        return quantity;
    }

    public StringFilter quantity() {
        if (quantity == null) {
            quantity = new StringFilter();
        }
        return quantity;
    }

    public void setQuantity(StringFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getRate() {
        return rate;
    }

    public StringFilter rate() {
        if (rate == null) {
            rate = new StringFilter();
        }
        return rate;
    }

    public void setRate(StringFilter rate) {
        this.rate = rate;
    }

    public DoubleFilter getPrixVente() {
        return prixVente;
    }

    public DoubleFilter prixVente() {
        if (prixVente == null) {
            prixVente = new DoubleFilter();
        }
        return prixVente;
    }

    public void setPrixVente(DoubleFilter prixVente) {
        this.prixVente = prixVente;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public OrderStatusFilter status() {
        if (status == null) {
            status = new OrderStatusFilter();
        }
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public LongFilter articleId() {
        if (articleId == null) {
            articleId = new LongFilter();
        }
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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
        final OrderItemCriteria that = (OrderItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(prixVente, that.prixVente) &&
            Objects.equals(status, that.status) &&
            Objects.equals(articleId, that.articleId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, rate, prixVente, status, articleId, orderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (rate != null ? "rate=" + rate + ", " : "") +
            (prixVente != null ? "prixVente=" + prixVente + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (articleId != null ? "articleId=" + articleId + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
