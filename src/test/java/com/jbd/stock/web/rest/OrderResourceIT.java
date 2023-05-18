package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Order;
import com.jbd.stock.domain.OrderItem;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.domain.enumeration.PaymentStatus;
import com.jbd.stock.domain.enumeration.PaymentType;
import com.jbd.stock.repository.OrderRepository;
import com.jbd.stock.service.criteria.OrderCriteria;
import com.jbd.stock.service.dto.OrderDTO;
import com.jbd.stock.service.mapper.OrderMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_CONTACT = "BBBBBBBBBB";

    private static final Double DEFAULT_SUB_TOTAL = 1D;
    private static final Double UPDATED_SUB_TOTAL = 2D;
    private static final Double SMALLER_SUB_TOTAL = 1D - 1D;

    private static final String DEFAULT_VAT = "AAAAAAAAAA";
    private static final String UPDATED_VAT = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;
    private static final Double SMALLER_TOTAL_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final String DEFAULT_GRAND_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_GRAND_TOTAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final String DEFAULT_DUE = "AAAAAAAAAA";
    private static final String UPDATED_DUE = "BBBBBBBBBB";

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.CHEQUE;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.CARD;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.FULL;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.ADVANCE;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .date(DEFAULT_DATE)
            .clientName(DEFAULT_CLIENT_NAME)
            .clientContact(DEFAULT_CLIENT_CONTACT)
            .subTotal(DEFAULT_SUB_TOTAL)
            .vat(DEFAULT_VAT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .discount(DEFAULT_DISCOUNT)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .paid(DEFAULT_PAID)
            .due(DEFAULT_DUE)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .status(DEFAULT_STATUS);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .date(UPDATED_DATE)
            .clientName(UPDATED_CLIENT_NAME)
            .clientContact(UPDATED_CLIENT_CONTACT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vat(UPDATED_VAT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .paid(UPDATED_PAID)
            .due(UPDATED_DUE)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .status(UPDATED_STATUS);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrder.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testOrder.getClientContact()).isEqualTo(DEFAULT_CLIENT_CONTACT);
        assertThat(testOrder.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testOrder.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testOrder.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrder.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testOrder.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testOrder.getDue()).isEqualTo(DEFAULT_DUE);
        assertThat(testOrder.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testOrder.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientContact").value(hasItem(DEFAULT_CLIENT_CONTACT)))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL)))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE)))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.clientContact").value(DEFAULT_CLIENT_CONTACT))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.grandTotal").value(DEFAULT_GRAND_TOTAL))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.due").value(DEFAULT_DUE))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date equals to DEFAULT_DATE
        defaultOrderShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the orderList where date equals to UPDATED_DATE
        defaultOrderShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOrderShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the orderList where date equals to UPDATED_DATE
        defaultOrderShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is not null
        defaultOrderShouldBeFound("date.specified=true");

        // Get all the orderList where date is null
        defaultOrderShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is greater than or equal to DEFAULT_DATE
        defaultOrderShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the orderList where date is greater than or equal to UPDATED_DATE
        defaultOrderShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is less than or equal to DEFAULT_DATE
        defaultOrderShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the orderList where date is less than or equal to SMALLER_DATE
        defaultOrderShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is less than DEFAULT_DATE
        defaultOrderShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the orderList where date is less than UPDATED_DATE
        defaultOrderShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where date is greater than DEFAULT_DATE
        defaultOrderShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the orderList where date is greater than SMALLER_DATE
        defaultOrderShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByClientNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientName equals to DEFAULT_CLIENT_NAME
        defaultOrderShouldBeFound("clientName.equals=" + DEFAULT_CLIENT_NAME);

        // Get all the orderList where clientName equals to UPDATED_CLIENT_NAME
        defaultOrderShouldNotBeFound("clientName.equals=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByClientNameIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientName in DEFAULT_CLIENT_NAME or UPDATED_CLIENT_NAME
        defaultOrderShouldBeFound("clientName.in=" + DEFAULT_CLIENT_NAME + "," + UPDATED_CLIENT_NAME);

        // Get all the orderList where clientName equals to UPDATED_CLIENT_NAME
        defaultOrderShouldNotBeFound("clientName.in=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByClientNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientName is not null
        defaultOrderShouldBeFound("clientName.specified=true");

        // Get all the orderList where clientName is null
        defaultOrderShouldNotBeFound("clientName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByClientNameContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientName contains DEFAULT_CLIENT_NAME
        defaultOrderShouldBeFound("clientName.contains=" + DEFAULT_CLIENT_NAME);

        // Get all the orderList where clientName contains UPDATED_CLIENT_NAME
        defaultOrderShouldNotBeFound("clientName.contains=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByClientNameNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientName does not contain DEFAULT_CLIENT_NAME
        defaultOrderShouldNotBeFound("clientName.doesNotContain=" + DEFAULT_CLIENT_NAME);

        // Get all the orderList where clientName does not contain UPDATED_CLIENT_NAME
        defaultOrderShouldBeFound("clientName.doesNotContain=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByClientContactIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientContact equals to DEFAULT_CLIENT_CONTACT
        defaultOrderShouldBeFound("clientContact.equals=" + DEFAULT_CLIENT_CONTACT);

        // Get all the orderList where clientContact equals to UPDATED_CLIENT_CONTACT
        defaultOrderShouldNotBeFound("clientContact.equals=" + UPDATED_CLIENT_CONTACT);
    }

    @Test
    @Transactional
    void getAllOrdersByClientContactIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientContact in DEFAULT_CLIENT_CONTACT or UPDATED_CLIENT_CONTACT
        defaultOrderShouldBeFound("clientContact.in=" + DEFAULT_CLIENT_CONTACT + "," + UPDATED_CLIENT_CONTACT);

        // Get all the orderList where clientContact equals to UPDATED_CLIENT_CONTACT
        defaultOrderShouldNotBeFound("clientContact.in=" + UPDATED_CLIENT_CONTACT);
    }

    @Test
    @Transactional
    void getAllOrdersByClientContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientContact is not null
        defaultOrderShouldBeFound("clientContact.specified=true");

        // Get all the orderList where clientContact is null
        defaultOrderShouldNotBeFound("clientContact.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByClientContactContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientContact contains DEFAULT_CLIENT_CONTACT
        defaultOrderShouldBeFound("clientContact.contains=" + DEFAULT_CLIENT_CONTACT);

        // Get all the orderList where clientContact contains UPDATED_CLIENT_CONTACT
        defaultOrderShouldNotBeFound("clientContact.contains=" + UPDATED_CLIENT_CONTACT);
    }

    @Test
    @Transactional
    void getAllOrdersByClientContactNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where clientContact does not contain DEFAULT_CLIENT_CONTACT
        defaultOrderShouldNotBeFound("clientContact.doesNotContain=" + DEFAULT_CLIENT_CONTACT);

        // Get all the orderList where clientContact does not contain UPDATED_CLIENT_CONTACT
        defaultOrderShouldBeFound("clientContact.doesNotContain=" + UPDATED_CLIENT_CONTACT);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the orderList where subTotal equals to UPDATED_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the orderList where subTotal equals to UPDATED_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal is not null
        defaultOrderShouldBeFound("subTotal.specified=true");

        // Get all the orderList where subTotal is null
        defaultOrderShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal is greater than or equal to DEFAULT_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.greaterThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the orderList where subTotal is greater than or equal to UPDATED_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.greaterThanOrEqual=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal is less than or equal to DEFAULT_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.lessThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the orderList where subTotal is less than or equal to SMALLER_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.lessThanOrEqual=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal is less than DEFAULT_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.lessThan=" + DEFAULT_SUB_TOTAL);

        // Get all the orderList where subTotal is less than UPDATED_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.lessThan=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersBySubTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where subTotal is greater than DEFAULT_SUB_TOTAL
        defaultOrderShouldNotBeFound("subTotal.greaterThan=" + DEFAULT_SUB_TOTAL);

        // Get all the orderList where subTotal is greater than SMALLER_SUB_TOTAL
        defaultOrderShouldBeFound("subTotal.greaterThan=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where vat equals to DEFAULT_VAT
        defaultOrderShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the orderList where vat equals to UPDATED_VAT
        defaultOrderShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllOrdersByVatIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultOrderShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the orderList where vat equals to UPDATED_VAT
        defaultOrderShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllOrdersByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where vat is not null
        defaultOrderShouldBeFound("vat.specified=true");

        // Get all the orderList where vat is null
        defaultOrderShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByVatContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where vat contains DEFAULT_VAT
        defaultOrderShouldBeFound("vat.contains=" + DEFAULT_VAT);

        // Get all the orderList where vat contains UPDATED_VAT
        defaultOrderShouldNotBeFound("vat.contains=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllOrdersByVatNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where vat does not contain DEFAULT_VAT
        defaultOrderShouldNotBeFound("vat.doesNotContain=" + DEFAULT_VAT);

        // Get all the orderList where vat does not contain UPDATED_VAT
        defaultOrderShouldBeFound("vat.doesNotContain=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is not null
        defaultOrderShouldBeFound("totalAmount.specified=true");

        // Get all the orderList where totalAmount is null
        defaultOrderShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultOrderShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the orderList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultOrderShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount equals to DEFAULT_DISCOUNT
        defaultOrderShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the orderList where discount equals to UPDATED_DISCOUNT
        defaultOrderShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultOrderShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the orderList where discount equals to UPDATED_DISCOUNT
        defaultOrderShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount is not null
        defaultOrderShouldBeFound("discount.specified=true");

        // Get all the orderList where discount is null
        defaultOrderShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultOrderShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the orderList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultOrderShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultOrderShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the orderList where discount is less than or equal to SMALLER_DISCOUNT
        defaultOrderShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount is less than DEFAULT_DISCOUNT
        defaultOrderShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the orderList where discount is less than UPDATED_DISCOUNT
        defaultOrderShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where discount is greater than DEFAULT_DISCOUNT
        defaultOrderShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the orderList where discount is greater than SMALLER_DISCOUNT
        defaultOrderShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal equals to DEFAULT_GRAND_TOTAL
        defaultOrderShouldBeFound("grandTotal.equals=" + DEFAULT_GRAND_TOTAL);

        // Get all the orderList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultOrderShouldNotBeFound("grandTotal.equals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal in DEFAULT_GRAND_TOTAL or UPDATED_GRAND_TOTAL
        defaultOrderShouldBeFound("grandTotal.in=" + DEFAULT_GRAND_TOTAL + "," + UPDATED_GRAND_TOTAL);

        // Get all the orderList where grandTotal equals to UPDATED_GRAND_TOTAL
        defaultOrderShouldNotBeFound("grandTotal.in=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is not null
        defaultOrderShouldBeFound("grandTotal.specified=true");

        // Get all the orderList where grandTotal is null
        defaultOrderShouldNotBeFound("grandTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal contains DEFAULT_GRAND_TOTAL
        defaultOrderShouldBeFound("grandTotal.contains=" + DEFAULT_GRAND_TOTAL);

        // Get all the orderList where grandTotal contains UPDATED_GRAND_TOTAL
        defaultOrderShouldNotBeFound("grandTotal.contains=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal does not contain DEFAULT_GRAND_TOTAL
        defaultOrderShouldNotBeFound("grandTotal.doesNotContain=" + DEFAULT_GRAND_TOTAL);

        // Get all the orderList where grandTotal does not contain UPDATED_GRAND_TOTAL
        defaultOrderShouldBeFound("grandTotal.doesNotContain=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paid equals to DEFAULT_PAID
        defaultOrderShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the orderList where paid equals to UPDATED_PAID
        defaultOrderShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    void getAllOrdersByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultOrderShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the orderList where paid equals to UPDATED_PAID
        defaultOrderShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    void getAllOrdersByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paid is not null
        defaultOrderShouldBeFound("paid.specified=true");

        // Get all the orderList where paid is null
        defaultOrderShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDueIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where due equals to DEFAULT_DUE
        defaultOrderShouldBeFound("due.equals=" + DEFAULT_DUE);

        // Get all the orderList where due equals to UPDATED_DUE
        defaultOrderShouldNotBeFound("due.equals=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllOrdersByDueIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where due in DEFAULT_DUE or UPDATED_DUE
        defaultOrderShouldBeFound("due.in=" + DEFAULT_DUE + "," + UPDATED_DUE);

        // Get all the orderList where due equals to UPDATED_DUE
        defaultOrderShouldNotBeFound("due.in=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllOrdersByDueIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where due is not null
        defaultOrderShouldBeFound("due.specified=true");

        // Get all the orderList where due is null
        defaultOrderShouldNotBeFound("due.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDueContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where due contains DEFAULT_DUE
        defaultOrderShouldBeFound("due.contains=" + DEFAULT_DUE);

        // Get all the orderList where due contains UPDATED_DUE
        defaultOrderShouldNotBeFound("due.contains=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllOrdersByDueNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where due does not contain DEFAULT_DUE
        defaultOrderShouldNotBeFound("due.doesNotContain=" + DEFAULT_DUE);

        // Get all the orderList where due does not contain UPDATED_DUE
        defaultOrderShouldBeFound("due.doesNotContain=" + UPDATED_DUE);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultOrderShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the orderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultOrderShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultOrderShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the orderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultOrderShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentType is not null
        defaultOrderShouldBeFound("paymentType.specified=true");

        // Get all the orderList where paymentType is null
        defaultOrderShouldNotBeFound("paymentType.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultOrderShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the orderList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultOrderShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultOrderShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the orderList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultOrderShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus is not null
        defaultOrderShouldBeFound("paymentStatus.specified=true");

        // Get all the orderList where paymentStatus is null
        defaultOrderShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status equals to DEFAULT_STATUS
        defaultOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the orderList where status equals to UPDATED_STATUS
        defaultOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the orderList where status equals to UPDATED_STATUS
        defaultOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status is not null
        defaultOrderShouldBeFound("status.specified=true");

        // Get all the orderList where status is null
        defaultOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByStatusContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status contains DEFAULT_STATUS
        defaultOrderShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the orderList where status contains UPDATED_STATUS
        defaultOrderShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status does not contain DEFAULT_STATUS
        defaultOrderShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the orderList where status does not contain UPDATED_STATUS
        defaultOrderShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        em.persist(orderItem);
        em.flush();
        order.addOrderItem(orderItem);
        orderRepository.saveAndFlush(order);
        Long orderItemId = orderItem.getId();

        // Get all the orderList where orderItem equals to orderItemId
        defaultOrderShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderList where orderItem equals to (orderItemId + 1)
        defaultOrderShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByUserIsEqualToSomething() throws Exception {
        Utilisateur user;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            user = UtilisateurResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(user);
        em.flush();
        order.setUser(user);
        orderRepository.saveAndFlush(order);
        Long userId = user.getId();

        // Get all the orderList where user equals to userId
        defaultOrderShouldBeFound("userId.equals=" + userId);

        // Get all the orderList where user equals to (userId + 1)
        defaultOrderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientContact").value(hasItem(DEFAULT_CLIENT_CONTACT)))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL)))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE)))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .date(UPDATED_DATE)
            .clientName(UPDATED_CLIENT_NAME)
            .clientContact(UPDATED_CLIENT_CONTACT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vat(UPDATED_VAT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .paid(UPDATED_PAID)
            .due(UPDATED_DUE)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .status(UPDATED_STATUS);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testOrder.getClientContact()).isEqualTo(UPDATED_CLIENT_CONTACT);
        assertThat(testOrder.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testOrder.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testOrder.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testOrder.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .date(UPDATED_DATE)
            .clientContact(UPDATED_CLIENT_CONTACT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vat(UPDATED_VAT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .paid(UPDATED_PAID)
            .due(UPDATED_DUE)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testOrder.getClientContact()).isEqualTo(UPDATED_CLIENT_CONTACT);
        assertThat(testOrder.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testOrder.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testOrder.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testOrder.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .date(UPDATED_DATE)
            .clientName(UPDATED_CLIENT_NAME)
            .clientContact(UPDATED_CLIENT_CONTACT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vat(UPDATED_VAT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .paid(UPDATED_PAID)
            .due(UPDATED_DUE)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .status(UPDATED_STATUS);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testOrder.getClientContact()).isEqualTo(UPDATED_CLIENT_CONTACT);
        assertThat(testOrder.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testOrder.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrder.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testOrder.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testOrder.getDue()).isEqualTo(UPDATED_DUE);
        assertThat(testOrder.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
