package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Paiement;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.repository.PaiementRepository;
import com.jbd.stock.service.criteria.PaiementCriteria;
import com.jbd.stock.service.dto.PaiementDTO;
import com.jbd.stock.service.mapper.PaiementMapper;
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
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final Double DEFAULT_REMUNURATION = 1D;
    private static final Double UPDATED_REMUNURATION = 2D;
    private static final Double SMALLER_REMUNURATION = 1D - 1D;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity(EntityManager em) {
        Paiement paiement = new Paiement().remunuration(DEFAULT_REMUNURATION).dateCreation(DEFAULT_DATE_CREATION);
        return paiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity(EntityManager em) {
        Paiement paiement = new Paiement().remunuration(UPDATED_REMUNURATION).dateCreation(UPDATED_DATE_CREATION);
        return paiement;
    }

    @BeforeEach
    public void initTest() {
        paiement = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        int databaseSizeBeforeCreate = paiementRepository.findAll().size();
        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isCreated());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate + 1);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getRemunuration()).isEqualTo(DEFAULT_REMUNURATION);
        assertThat(testPaiement.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        int databaseSizeBeforeCreate = paiementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].remunuration").value(hasItem(DEFAULT_REMUNURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.remunuration").value(DEFAULT_REMUNURATION.doubleValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getPaiementsByIdFiltering() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        Long id = paiement.getId();

        defaultPaiementShouldBeFound("id.equals=" + id);
        defaultPaiementShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration equals to DEFAULT_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.equals=" + DEFAULT_REMUNURATION);

        // Get all the paiementList where remunuration equals to UPDATED_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.equals=" + UPDATED_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration in DEFAULT_REMUNURATION or UPDATED_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.in=" + DEFAULT_REMUNURATION + "," + UPDATED_REMUNURATION);

        // Get all the paiementList where remunuration equals to UPDATED_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.in=" + UPDATED_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration is not null
        defaultPaiementShouldBeFound("remunuration.specified=true");

        // Get all the paiementList where remunuration is null
        defaultPaiementShouldNotBeFound("remunuration.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration is greater than or equal to DEFAULT_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.greaterThanOrEqual=" + DEFAULT_REMUNURATION);

        // Get all the paiementList where remunuration is greater than or equal to UPDATED_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.greaterThanOrEqual=" + UPDATED_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration is less than or equal to DEFAULT_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.lessThanOrEqual=" + DEFAULT_REMUNURATION);

        // Get all the paiementList where remunuration is less than or equal to SMALLER_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.lessThanOrEqual=" + SMALLER_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsLessThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration is less than DEFAULT_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.lessThan=" + DEFAULT_REMUNURATION);

        // Get all the paiementList where remunuration is less than UPDATED_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.lessThan=" + UPDATED_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByRemunurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where remunuration is greater than DEFAULT_REMUNURATION
        defaultPaiementShouldNotBeFound("remunuration.greaterThan=" + DEFAULT_REMUNURATION);

        // Get all the paiementList where remunuration is greater than SMALLER_REMUNURATION
        defaultPaiementShouldBeFound("remunuration.greaterThan=" + SMALLER_REMUNURATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the paiementList where dateCreation equals to UPDATED_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the paiementList where dateCreation equals to UPDATED_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation is not null
        defaultPaiementShouldBeFound("dateCreation.specified=true");

        // Get all the paiementList where dateCreation is null
        defaultPaiementShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the paiementList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the paiementList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the paiementList where dateCreation is less than UPDATED_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultPaiementShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the paiementList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultPaiementShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByUserIsEqualToSomething() throws Exception {
        Utilisateur user;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            paiementRepository.saveAndFlush(paiement);
            user = UtilisateurResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(user);
        em.flush();
        paiement.setUser(user);
        paiementRepository.saveAndFlush(paiement);
        Long userId = user.getId();

        // Get all the paiementList where user equals to userId
        defaultPaiementShouldBeFound("userId.equals=" + userId);

        // Get all the paiementList where user equals to (userId + 1)
        defaultPaiementShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementShouldBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].remunuration").value(hasItem(DEFAULT_REMUNURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementShouldNotBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).get();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement.remunuration(UPDATED_REMUNURATION).dateCreation(UPDATED_DATE_CREATION);
        PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getRemunuration()).isEqualTo(UPDATED_REMUNURATION);
        assertThat(testPaiement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.dateCreation(UPDATED_DATE_CREATION);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getRemunuration()).isEqualTo(DEFAULT_REMUNURATION);
        assertThat(testPaiement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.remunuration(UPDATED_REMUNURATION).dateCreation(UPDATED_DATE_CREATION);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getRemunuration()).isEqualTo(UPDATED_REMUNURATION);
        assertThat(testPaiement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeDelete = paiementRepository.findAll().size();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
