package com.jbd.stock.service;

import com.jbd.stock.domain.*; // for static metamodels
import com.jbd.stock.domain.Order;
import com.jbd.stock.repository.OrderRepository;
import com.jbd.stock.service.criteria.OrderCriteria;
import com.jbd.stock.service.dto.OrderDTO;
import com.jbd.stock.service.mapper.OrderMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Order} entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderDTO} or a {@link Page} of {@link OrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderQueryService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Return a {@link List} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderMapper.toDto(orderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page).map(orderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Order_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Order_.date));
            }
            if (criteria.getClientName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientName(), Order_.clientName));
            }
            if (criteria.getClientContact() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientContact(), Order_.clientContact));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), Order_.subTotal));
            }
            if (criteria.getVat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVat(), Order_.vat));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), Order_.totalAmount));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), Order_.discount));
            }
            if (criteria.getGrandTotal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrandTotal(), Order_.grandTotal));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildSpecification(criteria.getPaid(), Order_.paid));
            }
            if (criteria.getDue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDue(), Order_.due));
            }
            if (criteria.getPaymentType() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentType(), Order_.paymentType));
            }
            if (criteria.getPaymentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentStatus(), Order_.paymentStatus));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Order_.status));
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(Order_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Order_.user, JoinType.LEFT).get(Utilisateur_.id))
                    );
            }
        }
        return specification;
    }
}
