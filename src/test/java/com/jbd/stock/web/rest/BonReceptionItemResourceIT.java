package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.BonReception;
import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.repository.BonReceptionItemRepository;
import com.jbd.stock.service.criteria.BonReceptionItemCriteria;
import com.jbd.stock.service.dto.BonReceptionItemDTO;
import com.jbd.stock.service.mapper.BonReceptionItemMapper;
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
 * Integration tests for the {@link BonReceptionItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BonReceptionItemResourceIT {

    private static final Double DEFAULT_QTE = 1D;
    private static final Double UPDATED_QTE = 2D;
    private static final Double SMALLER_QTE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/bon-reception-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonReceptionItemRepository bonReceptionItemRepository;

    @Autowired
    private BonReceptionItemMapper bonReceptionItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonReceptionItemMockMvc;

    private BonReceptionItem bonReceptionItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonReceptionItem createEntity(EntityManager em) {
        BonReceptionItem bonReceptionItem = new BonReceptionItem().qte(DEFAULT_QTE);
        return bonReceptionItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonReceptionItem createUpdatedEntity(EntityManager em) {
        BonReceptionItem bonReceptionItem = new BonReceptionItem().qte(UPDATED_QTE);
        return bonReceptionItem;
    }

    @BeforeEach
    public void initTest() {
        bonReceptionItem = createEntity(em);
    }

    @Test
    @Transactional
    void createBonReceptionItem() throws Exception {
        int databaseSizeBeforeCreate = bonReceptionItemRepository.findAll().size();
        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);
        restBonReceptionItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeCreate + 1);
        BonReceptionItem testBonReceptionItem = bonReceptionItemList.get(bonReceptionItemList.size() - 1);
        assertThat(testBonReceptionItem.getQte()).isEqualTo(DEFAULT_QTE);
    }

    @Test
    @Transactional
    void createBonReceptionItemWithExistingId() throws Exception {
        // Create the BonReceptionItem with an existing ID
        bonReceptionItem.setId(1L);
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        int databaseSizeBeforeCreate = bonReceptionItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonReceptionItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBonReceptionItems() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonReceptionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE.doubleValue())));
    }

    @Test
    @Transactional
    void getBonReceptionItem() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get the bonReceptionItem
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL_ID, bonReceptionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bonReceptionItem.getId().intValue()))
            .andExpect(jsonPath("$.qte").value(DEFAULT_QTE.doubleValue()));
    }

    @Test
    @Transactional
    void getBonReceptionItemsByIdFiltering() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        Long id = bonReceptionItem.getId();

        defaultBonReceptionItemShouldBeFound("id.equals=" + id);
        defaultBonReceptionItemShouldNotBeFound("id.notEquals=" + id);

        defaultBonReceptionItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBonReceptionItemShouldNotBeFound("id.greaterThan=" + id);

        defaultBonReceptionItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBonReceptionItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte equals to DEFAULT_QTE
        defaultBonReceptionItemShouldBeFound("qte.equals=" + DEFAULT_QTE);

        // Get all the bonReceptionItemList where qte equals to UPDATED_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.equals=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsInShouldWork() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte in DEFAULT_QTE or UPDATED_QTE
        defaultBonReceptionItemShouldBeFound("qte.in=" + DEFAULT_QTE + "," + UPDATED_QTE);

        // Get all the bonReceptionItemList where qte equals to UPDATED_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.in=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsNullOrNotNull() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte is not null
        defaultBonReceptionItemShouldBeFound("qte.specified=true");

        // Get all the bonReceptionItemList where qte is null
        defaultBonReceptionItemShouldNotBeFound("qte.specified=false");
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte is greater than or equal to DEFAULT_QTE
        defaultBonReceptionItemShouldBeFound("qte.greaterThanOrEqual=" + DEFAULT_QTE);

        // Get all the bonReceptionItemList where qte is greater than or equal to UPDATED_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.greaterThanOrEqual=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte is less than or equal to DEFAULT_QTE
        defaultBonReceptionItemShouldBeFound("qte.lessThanOrEqual=" + DEFAULT_QTE);

        // Get all the bonReceptionItemList where qte is less than or equal to SMALLER_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.lessThanOrEqual=" + SMALLER_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsLessThanSomething() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte is less than DEFAULT_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.lessThan=" + DEFAULT_QTE);

        // Get all the bonReceptionItemList where qte is less than UPDATED_QTE
        defaultBonReceptionItemShouldBeFound("qte.lessThan=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByQteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        // Get all the bonReceptionItemList where qte is greater than DEFAULT_QTE
        defaultBonReceptionItemShouldNotBeFound("qte.greaterThan=" + DEFAULT_QTE);

        // Get all the bonReceptionItemList where qte is greater than SMALLER_QTE
        defaultBonReceptionItemShouldBeFound("qte.greaterThan=" + SMALLER_QTE);
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByBonIsEqualToSomething() throws Exception {
        BonReception bon;
        if (TestUtil.findAll(em, BonReception.class).isEmpty()) {
            bonReceptionItemRepository.saveAndFlush(bonReceptionItem);
            bon = BonReceptionResourceIT.createEntity(em);
        } else {
            bon = TestUtil.findAll(em, BonReception.class).get(0);
        }
        em.persist(bon);
        em.flush();
        bonReceptionItem.setBon(bon);
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);
        Long bonId = bon.getId();

        // Get all the bonReceptionItemList where bon equals to bonId
        defaultBonReceptionItemShouldBeFound("bonId.equals=" + bonId);

        // Get all the bonReceptionItemList where bon equals to (bonId + 1)
        defaultBonReceptionItemShouldNotBeFound("bonId.equals=" + (bonId + 1));
    }

    @Test
    @Transactional
    void getAllBonReceptionItemsByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            bonReceptionItemRepository.saveAndFlush(bonReceptionItem);
            article = ArticleResourceIT.createEntity(em);
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        bonReceptionItem.setArticle(article);
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);
        Long articleId = article.getId();

        // Get all the bonReceptionItemList where article equals to articleId
        defaultBonReceptionItemShouldBeFound("articleId.equals=" + articleId);

        // Get all the bonReceptionItemList where article equals to (articleId + 1)
        defaultBonReceptionItemShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBonReceptionItemShouldBeFound(String filter) throws Exception {
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonReceptionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE.doubleValue())));

        // Check, that the count call also returns 1
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBonReceptionItemShouldNotBeFound(String filter) throws Exception {
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBonReceptionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBonReceptionItem() throws Exception {
        // Get the bonReceptionItem
        restBonReceptionItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBonReceptionItem() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();

        // Update the bonReceptionItem
        BonReceptionItem updatedBonReceptionItem = bonReceptionItemRepository.findById(bonReceptionItem.getId()).get();
        // Disconnect from session so that the updates on updatedBonReceptionItem are not directly saved in db
        em.detach(updatedBonReceptionItem);
        updatedBonReceptionItem.qte(UPDATED_QTE);
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(updatedBonReceptionItem);

        restBonReceptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonReceptionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
        BonReceptionItem testBonReceptionItem = bonReceptionItemList.get(bonReceptionItemList.size() - 1);
        assertThat(testBonReceptionItem.getQte()).isEqualTo(UPDATED_QTE);
    }

    @Test
    @Transactional
    void putNonExistingBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonReceptionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonReceptionItemWithPatch() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();

        // Update the bonReceptionItem using partial update
        BonReceptionItem partialUpdatedBonReceptionItem = new BonReceptionItem();
        partialUpdatedBonReceptionItem.setId(bonReceptionItem.getId());

        restBonReceptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonReceptionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonReceptionItem))
            )
            .andExpect(status().isOk());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
        BonReceptionItem testBonReceptionItem = bonReceptionItemList.get(bonReceptionItemList.size() - 1);
        assertThat(testBonReceptionItem.getQte()).isEqualTo(DEFAULT_QTE);
    }

    @Test
    @Transactional
    void fullUpdateBonReceptionItemWithPatch() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();

        // Update the bonReceptionItem using partial update
        BonReceptionItem partialUpdatedBonReceptionItem = new BonReceptionItem();
        partialUpdatedBonReceptionItem.setId(bonReceptionItem.getId());

        partialUpdatedBonReceptionItem.qte(UPDATED_QTE);

        restBonReceptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonReceptionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonReceptionItem))
            )
            .andExpect(status().isOk());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
        BonReceptionItem testBonReceptionItem = bonReceptionItemList.get(bonReceptionItemList.size() - 1);
        assertThat(testBonReceptionItem.getQte()).isEqualTo(UPDATED_QTE);
    }

    @Test
    @Transactional
    void patchNonExistingBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bonReceptionItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBonReceptionItem() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionItemRepository.findAll().size();
        bonReceptionItem.setId(count.incrementAndGet());

        // Create the BonReceptionItem
        BonReceptionItemDTO bonReceptionItemDTO = bonReceptionItemMapper.toDto(bonReceptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonReceptionItem in the database
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBonReceptionItem() throws Exception {
        // Initialize the database
        bonReceptionItemRepository.saveAndFlush(bonReceptionItem);

        int databaseSizeBeforeDelete = bonReceptionItemRepository.findAll().size();

        // Delete the bonReceptionItem
        restBonReceptionItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, bonReceptionItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BonReceptionItem> bonReceptionItemList = bonReceptionItemRepository.findAll();
        assertThat(bonReceptionItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
