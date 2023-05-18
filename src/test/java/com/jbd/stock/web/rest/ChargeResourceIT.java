package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Charge;
import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.repository.ChargeRepository;
import com.jbd.stock.service.criteria.ChargeCriteria;
import com.jbd.stock.service.dto.ChargeDTO;
import com.jbd.stock.service.mapper.ChargeMapper;
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
 * Integration tests for the {@link ChargeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChargeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Double DEFAULT_VALEUR = 1D;
    private static final Double UPDATED_VALEUR = 2D;
    private static final Double SMALLER_VALEUR = 1D - 1D;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChargeMockMvc;

    private Charge charge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charge createEntity(EntityManager em) {
        Charge charge = new Charge().nom(DEFAULT_NOM).valeur(DEFAULT_VALEUR).dateCreation(DEFAULT_DATE_CREATION);
        return charge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charge createUpdatedEntity(EntityManager em) {
        Charge charge = new Charge().nom(UPDATED_NOM).valeur(UPDATED_VALEUR).dateCreation(UPDATED_DATE_CREATION);
        return charge;
    }

    @BeforeEach
    public void initTest() {
        charge = createEntity(em);
    }

    @Test
    @Transactional
    void createCharge() throws Exception {
        int databaseSizeBeforeCreate = chargeRepository.findAll().size();
        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);
        restChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isCreated());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate + 1);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCharge.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testCharge.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createChargeWithExistingId() throws Exception {
        // Create the Charge with an existing ID
        charge.setId(1L);
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCharges() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList
        restChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get the charge
        restChargeMockMvc
            .perform(get(ENTITY_API_URL_ID, charge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(charge.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getChargesByIdFiltering() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        Long id = charge.getId();

        defaultChargeShouldBeFound("id.equals=" + id);
        defaultChargeShouldNotBeFound("id.notEquals=" + id);

        defaultChargeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChargeShouldNotBeFound("id.greaterThan=" + id);

        defaultChargeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChargeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChargesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where nom equals to DEFAULT_NOM
        defaultChargeShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the chargeList where nom equals to UPDATED_NOM
        defaultChargeShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllChargesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultChargeShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the chargeList where nom equals to UPDATED_NOM
        defaultChargeShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllChargesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where nom is not null
        defaultChargeShouldBeFound("nom.specified=true");

        // Get all the chargeList where nom is null
        defaultChargeShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllChargesByNomContainsSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where nom contains DEFAULT_NOM
        defaultChargeShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the chargeList where nom contains UPDATED_NOM
        defaultChargeShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllChargesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where nom does not contain DEFAULT_NOM
        defaultChargeShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the chargeList where nom does not contain UPDATED_NOM
        defaultChargeShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur equals to DEFAULT_VALEUR
        defaultChargeShouldBeFound("valeur.equals=" + DEFAULT_VALEUR);

        // Get all the chargeList where valeur equals to UPDATED_VALEUR
        defaultChargeShouldNotBeFound("valeur.equals=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsInShouldWork() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur in DEFAULT_VALEUR or UPDATED_VALEUR
        defaultChargeShouldBeFound("valeur.in=" + DEFAULT_VALEUR + "," + UPDATED_VALEUR);

        // Get all the chargeList where valeur equals to UPDATED_VALEUR
        defaultChargeShouldNotBeFound("valeur.in=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur is not null
        defaultChargeShouldBeFound("valeur.specified=true");

        // Get all the chargeList where valeur is null
        defaultChargeShouldNotBeFound("valeur.specified=false");
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur is greater than or equal to DEFAULT_VALEUR
        defaultChargeShouldBeFound("valeur.greaterThanOrEqual=" + DEFAULT_VALEUR);

        // Get all the chargeList where valeur is greater than or equal to UPDATED_VALEUR
        defaultChargeShouldNotBeFound("valeur.greaterThanOrEqual=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur is less than or equal to DEFAULT_VALEUR
        defaultChargeShouldBeFound("valeur.lessThanOrEqual=" + DEFAULT_VALEUR);

        // Get all the chargeList where valeur is less than or equal to SMALLER_VALEUR
        defaultChargeShouldNotBeFound("valeur.lessThanOrEqual=" + SMALLER_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsLessThanSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur is less than DEFAULT_VALEUR
        defaultChargeShouldNotBeFound("valeur.lessThan=" + DEFAULT_VALEUR);

        // Get all the chargeList where valeur is less than UPDATED_VALEUR
        defaultChargeShouldBeFound("valeur.lessThan=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByValeurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where valeur is greater than DEFAULT_VALEUR
        defaultChargeShouldNotBeFound("valeur.greaterThan=" + DEFAULT_VALEUR);

        // Get all the chargeList where valeur is greater than SMALLER_VALEUR
        defaultChargeShouldBeFound("valeur.greaterThan=" + SMALLER_VALEUR);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the chargeList where dateCreation equals to UPDATED_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the chargeList where dateCreation equals to UPDATED_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation is not null
        defaultChargeShouldBeFound("dateCreation.specified=true");

        // Get all the chargeList where dateCreation is null
        defaultChargeShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the chargeList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the chargeList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the chargeList where dateCreation is less than UPDATED_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultChargeShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the chargeList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultChargeShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllChargesByTypeIsEqualToSomething() throws Exception {
        TypeCharge type;
        if (TestUtil.findAll(em, TypeCharge.class).isEmpty()) {
            chargeRepository.saveAndFlush(charge);
            type = TypeChargeResourceIT.createEntity(em);
        } else {
            type = TestUtil.findAll(em, TypeCharge.class).get(0);
        }
        em.persist(type);
        em.flush();
        charge.setType(type);
        chargeRepository.saveAndFlush(charge);
        Long typeId = type.getId();

        // Get all the chargeList where type equals to typeId
        defaultChargeShouldBeFound("typeId.equals=" + typeId);

        // Get all the chargeList where type equals to (typeId + 1)
        defaultChargeShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChargeShouldBeFound(String filter) throws Exception {
        restChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restChargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChargeShouldNotBeFound(String filter) throws Exception {
        restChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCharge() throws Exception {
        // Get the charge
        restChargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge
        Charge updatedCharge = chargeRepository.findById(charge.getId()).get();
        // Disconnect from session so that the updates on updatedCharge are not directly saved in db
        em.detach(updatedCharge);
        updatedCharge.nom(UPDATED_NOM).valeur(UPDATED_VALEUR).dateCreation(UPDATED_DATE_CREATION);
        ChargeDTO chargeDTO = chargeMapper.toDto(updatedCharge);

        restChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCharge.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testCharge.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChargeWithPatch() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge using partial update
        Charge partialUpdatedCharge = new Charge();
        partialUpdatedCharge.setId(charge.getId());

        partialUpdatedCharge.dateCreation(UPDATED_DATE_CREATION);

        restChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharge))
            )
            .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCharge.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testCharge.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateChargeWithPatch() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge using partial update
        Charge partialUpdatedCharge = new Charge();
        partialUpdatedCharge.setId(charge.getId());

        partialUpdatedCharge.nom(UPDATED_NOM).valeur(UPDATED_VALEUR).dateCreation(UPDATED_DATE_CREATION);

        restChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharge))
            )
            .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCharge.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testCharge.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chargeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();
        charge.setId(count.incrementAndGet());

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chargeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        int databaseSizeBeforeDelete = chargeRepository.findAll().size();

        // Delete the charge
        restChargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, charge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
