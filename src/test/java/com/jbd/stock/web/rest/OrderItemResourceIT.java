package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.Order;
import com.jbd.stock.domain.OrderItem;
import com.jbd.stock.domain.enumeration.OrderStatus;
import com.jbd.stock.repository.OrderItemRepository;
import com.jbd.stock.service.criteria.OrderItemCriteria;
import com.jbd.stock.service.dto.OrderItemDTO;
import com.jbd.stock.service.mapper.OrderItemMapper;
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
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final String DEFAULT_RATE = "AAAAAAAAAA";
    private static final String UPDATED_RATE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_VENTE = 1D;
    private static final Double UPDATED_PRIX_VENTE = 2D;
    private static final Double SMALLER_PRIX_VENTE = 1D - 1D;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.STATUS1;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.STATUS1;

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .quantity(DEFAULT_QUANTITY)
            .rate(DEFAULT_RATE)
            .prixVente(DEFAULT_PRIX_VENTE)
            .status(DEFAULT_STATUS);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .quantity(UPDATED_QUANTITY)
            .rate(UPDATED_RATE)
            .prixVente(UPDATED_PRIX_VENTE)
            .status(UPDATED_STATUS);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderItem.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testOrderItem.getPrixVente()).isEqualTo(DEFAULT_PRIX_VENTE);
        assertThat(testOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItem.setId(1L);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.prixVente").value(DEFAULT_PRIX_VENTE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        Long id = orderItem.getId();

        defaultOrderItemShouldBeFound("id.equals=" + id);
        defaultOrderItemShouldNotBeFound("id.notEquals=" + id);

        defaultOrderItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity equals to DEFAULT_QUANTITY
        defaultOrderItemShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderItemList where quantity equals to UPDATED_QUANTITY
        defaultOrderItemShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderItemShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderItemList where quantity equals to UPDATED_QUANTITY
        defaultOrderItemShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is not null
        defaultOrderItemShouldBeFound("quantity.specified=true");

        // Get all the orderItemList where quantity is null
        defaultOrderItemShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity contains DEFAULT_QUANTITY
        defaultOrderItemShouldBeFound("quantity.contains=" + DEFAULT_QUANTITY);

        // Get all the orderItemList where quantity contains UPDATED_QUANTITY
        defaultOrderItemShouldNotBeFound("quantity.contains=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity does not contain DEFAULT_QUANTITY
        defaultOrderItemShouldNotBeFound("quantity.doesNotContain=" + DEFAULT_QUANTITY);

        // Get all the orderItemList where quantity does not contain UPDATED_QUANTITY
        defaultOrderItemShouldBeFound("quantity.doesNotContain=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where rate equals to DEFAULT_RATE
        defaultOrderItemShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the orderItemList where rate equals to UPDATED_RATE
        defaultOrderItemShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultOrderItemShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the orderItemList where rate equals to UPDATED_RATE
        defaultOrderItemShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where rate is not null
        defaultOrderItemShouldBeFound("rate.specified=true");

        // Get all the orderItemList where rate is null
        defaultOrderItemShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByRateContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where rate contains DEFAULT_RATE
        defaultOrderItemShouldBeFound("rate.contains=" + DEFAULT_RATE);

        // Get all the orderItemList where rate contains UPDATED_RATE
        defaultOrderItemShouldNotBeFound("rate.contains=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByRateNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where rate does not contain DEFAULT_RATE
        defaultOrderItemShouldNotBeFound("rate.doesNotContain=" + DEFAULT_RATE);

        // Get all the orderItemList where rate does not contain UPDATED_RATE
        defaultOrderItemShouldBeFound("rate.doesNotContain=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente equals to DEFAULT_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.equals=" + DEFAULT_PRIX_VENTE);

        // Get all the orderItemList where prixVente equals to UPDATED_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.equals=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente in DEFAULT_PRIX_VENTE or UPDATED_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.in=" + DEFAULT_PRIX_VENTE + "," + UPDATED_PRIX_VENTE);

        // Get all the orderItemList where prixVente equals to UPDATED_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.in=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente is not null
        defaultOrderItemShouldBeFound("prixVente.specified=true");

        // Get all the orderItemList where prixVente is null
        defaultOrderItemShouldNotBeFound("prixVente.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente is greater than or equal to DEFAULT_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.greaterThanOrEqual=" + DEFAULT_PRIX_VENTE);

        // Get all the orderItemList where prixVente is greater than or equal to UPDATED_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.greaterThanOrEqual=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente is less than or equal to DEFAULT_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.lessThanOrEqual=" + DEFAULT_PRIX_VENTE);

        // Get all the orderItemList where prixVente is less than or equal to SMALLER_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.lessThanOrEqual=" + SMALLER_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente is less than DEFAULT_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.lessThan=" + DEFAULT_PRIX_VENTE);

        // Get all the orderItemList where prixVente is less than UPDATED_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.lessThan=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByPrixVenteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where prixVente is greater than DEFAULT_PRIX_VENTE
        defaultOrderItemShouldNotBeFound("prixVente.greaterThan=" + DEFAULT_PRIX_VENTE);

        // Get all the orderItemList where prixVente is greater than SMALLER_PRIX_VENTE
        defaultOrderItemShouldBeFound("prixVente.greaterThan=" + SMALLER_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where status equals to DEFAULT_STATUS
        defaultOrderItemShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the orderItemList where status equals to UPDATED_STATUS
        defaultOrderItemShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrderItemShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the orderItemList where status equals to UPDATED_STATUS
        defaultOrderItemShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where status is not null
        defaultOrderItemShouldBeFound("status.specified=true");

        // Get all the orderItemList where status is null
        defaultOrderItemShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            article = ArticleResourceIT.createEntity(em);
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        orderItem.setArticle(article);
        orderItemRepository.saveAndFlush(orderItem);
        Long articleId = article.getId();

        // Get all the orderItemList where article equals to articleId
        defaultOrderItemShouldBeFound("articleId.equals=" + articleId);

        // Get all the orderItemList where article equals to (articleId + 1)
        defaultOrderItemShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        orderItem.setOrder(order);
        orderItemRepository.saveAndFlush(orderItem);
        Long orderId = order.getId();

        // Get all the orderItemList where order equals to orderId
        defaultOrderItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderItemList where order equals to (orderId + 1)
        defaultOrderItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemShouldBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemShouldNotBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem.quantity(UPDATED_QUANTITY).rate(UPDATED_RATE).prixVente(UPDATED_PRIX_VENTE).status(UPDATED_STATUS);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(updatedOrderItem);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testOrderItem.getPrixVente()).isEqualTo(UPDATED_PRIX_VENTE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem.quantity(UPDATED_QUANTITY).prixVente(UPDATED_PRIX_VENTE).status(UPDATED_STATUS);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testOrderItem.getPrixVente()).isEqualTo(UPDATED_PRIX_VENTE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem.quantity(UPDATED_QUANTITY).rate(UPDATED_RATE).prixVente(UPDATED_PRIX_VENTE).status(UPDATED_STATUS);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testOrderItem.getPrixVente()).isEqualTo(UPDATED_PRIX_VENTE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
