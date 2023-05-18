package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.BonReception;
import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.repository.BonReceptionRepository;
import com.jbd.stock.service.criteria.BonReceptionCriteria;
import com.jbd.stock.service.dto.BonReceptionDTO;
import com.jbd.stock.service.mapper.BonReceptionMapper;
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
 * Integration tests for the {@link BonReceptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BonReceptionResourceIT {

    private static final String DEFAULT_INFORMATON = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATON = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_FACTURE = 1;
    private static final Integer UPDATED_NUM_FACTURE = 2;
    private static final Integer SMALLER_NUM_FACTURE = 1 - 1;

    private static final Integer DEFAULT_NUM_BL = 1;
    private static final Integer UPDATED_NUM_BL = 2;
    private static final Integer SMALLER_NUM_BL = 1 - 1;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/bon-receptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonReceptionRepository bonReceptionRepository;

    @Autowired
    private BonReceptionMapper bonReceptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonReceptionMockMvc;

    private BonReception bonReception;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonReception createEntity(EntityManager em) {
        BonReception bonReception = new BonReception()
            .informaton(DEFAULT_INFORMATON)
            .numFacture(DEFAULT_NUM_FACTURE)
            .numBl(DEFAULT_NUM_BL)
            .dateCreation(DEFAULT_DATE_CREATION);
        return bonReception;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonReception createUpdatedEntity(EntityManager em) {
        BonReception bonReception = new BonReception()
            .informaton(UPDATED_INFORMATON)
            .numFacture(UPDATED_NUM_FACTURE)
            .numBl(UPDATED_NUM_BL)
            .dateCreation(UPDATED_DATE_CREATION);
        return bonReception;
    }

    @BeforeEach
    public void initTest() {
        bonReception = createEntity(em);
    }

    @Test
    @Transactional
    void createBonReception() throws Exception {
        int databaseSizeBeforeCreate = bonReceptionRepository.findAll().size();
        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);
        restBonReceptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeCreate + 1);
        BonReception testBonReception = bonReceptionList.get(bonReceptionList.size() - 1);
        assertThat(testBonReception.getInformaton()).isEqualTo(DEFAULT_INFORMATON);
        assertThat(testBonReception.getNumFacture()).isEqualTo(DEFAULT_NUM_FACTURE);
        assertThat(testBonReception.getNumBl()).isEqualTo(DEFAULT_NUM_BL);
        assertThat(testBonReception.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createBonReceptionWithExistingId() throws Exception {
        // Create the BonReception with an existing ID
        bonReception.setId(1L);
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        int databaseSizeBeforeCreate = bonReceptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonReceptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBonReceptions() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonReception.getId().intValue())))
            .andExpect(jsonPath("$.[*].informaton").value(hasItem(DEFAULT_INFORMATON)))
            .andExpect(jsonPath("$.[*].numFacture").value(hasItem(DEFAULT_NUM_FACTURE)))
            .andExpect(jsonPath("$.[*].numBl").value(hasItem(DEFAULT_NUM_BL)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getBonReception() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get the bonReception
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL_ID, bonReception.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bonReception.getId().intValue()))
            .andExpect(jsonPath("$.informaton").value(DEFAULT_INFORMATON))
            .andExpect(jsonPath("$.numFacture").value(DEFAULT_NUM_FACTURE))
            .andExpect(jsonPath("$.numBl").value(DEFAULT_NUM_BL))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getBonReceptionsByIdFiltering() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        Long id = bonReception.getId();

        defaultBonReceptionShouldBeFound("id.equals=" + id);
        defaultBonReceptionShouldNotBeFound("id.notEquals=" + id);

        defaultBonReceptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBonReceptionShouldNotBeFound("id.greaterThan=" + id);

        defaultBonReceptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBonReceptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByInformatonIsEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where informaton equals to DEFAULT_INFORMATON
        defaultBonReceptionShouldBeFound("informaton.equals=" + DEFAULT_INFORMATON);

        // Get all the bonReceptionList where informaton equals to UPDATED_INFORMATON
        defaultBonReceptionShouldNotBeFound("informaton.equals=" + UPDATED_INFORMATON);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByInformatonIsInShouldWork() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where informaton in DEFAULT_INFORMATON or UPDATED_INFORMATON
        defaultBonReceptionShouldBeFound("informaton.in=" + DEFAULT_INFORMATON + "," + UPDATED_INFORMATON);

        // Get all the bonReceptionList where informaton equals to UPDATED_INFORMATON
        defaultBonReceptionShouldNotBeFound("informaton.in=" + UPDATED_INFORMATON);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByInformatonIsNullOrNotNull() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where informaton is not null
        defaultBonReceptionShouldBeFound("informaton.specified=true");

        // Get all the bonReceptionList where informaton is null
        defaultBonReceptionShouldNotBeFound("informaton.specified=false");
    }

    @Test
    @Transactional
    void getAllBonReceptionsByInformatonContainsSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where informaton contains DEFAULT_INFORMATON
        defaultBonReceptionShouldBeFound("informaton.contains=" + DEFAULT_INFORMATON);

        // Get all the bonReceptionList where informaton contains UPDATED_INFORMATON
        defaultBonReceptionShouldNotBeFound("informaton.contains=" + UPDATED_INFORMATON);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByInformatonNotContainsSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where informaton does not contain DEFAULT_INFORMATON
        defaultBonReceptionShouldNotBeFound("informaton.doesNotContain=" + DEFAULT_INFORMATON);

        // Get all the bonReceptionList where informaton does not contain UPDATED_INFORMATON
        defaultBonReceptionShouldBeFound("informaton.doesNotContain=" + UPDATED_INFORMATON);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture equals to DEFAULT_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.equals=" + DEFAULT_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture equals to UPDATED_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.equals=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsInShouldWork() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture in DEFAULT_NUM_FACTURE or UPDATED_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.in=" + DEFAULT_NUM_FACTURE + "," + UPDATED_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture equals to UPDATED_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.in=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture is not null
        defaultBonReceptionShouldBeFound("numFacture.specified=true");

        // Get all the bonReceptionList where numFacture is null
        defaultBonReceptionShouldNotBeFound("numFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture is greater than or equal to DEFAULT_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.greaterThanOrEqual=" + DEFAULT_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture is greater than or equal to UPDATED_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.greaterThanOrEqual=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture is less than or equal to DEFAULT_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.lessThanOrEqual=" + DEFAULT_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture is less than or equal to SMALLER_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.lessThanOrEqual=" + SMALLER_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsLessThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture is less than DEFAULT_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.lessThan=" + DEFAULT_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture is less than UPDATED_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.lessThan=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumFactureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numFacture is greater than DEFAULT_NUM_FACTURE
        defaultBonReceptionShouldNotBeFound("numFacture.greaterThan=" + DEFAULT_NUM_FACTURE);

        // Get all the bonReceptionList where numFacture is greater than SMALLER_NUM_FACTURE
        defaultBonReceptionShouldBeFound("numFacture.greaterThan=" + SMALLER_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl equals to DEFAULT_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.equals=" + DEFAULT_NUM_BL);

        // Get all the bonReceptionList where numBl equals to UPDATED_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.equals=" + UPDATED_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsInShouldWork() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl in DEFAULT_NUM_BL or UPDATED_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.in=" + DEFAULT_NUM_BL + "," + UPDATED_NUM_BL);

        // Get all the bonReceptionList where numBl equals to UPDATED_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.in=" + UPDATED_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsNullOrNotNull() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl is not null
        defaultBonReceptionShouldBeFound("numBl.specified=true");

        // Get all the bonReceptionList where numBl is null
        defaultBonReceptionShouldNotBeFound("numBl.specified=false");
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl is greater than or equal to DEFAULT_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.greaterThanOrEqual=" + DEFAULT_NUM_BL);

        // Get all the bonReceptionList where numBl is greater than or equal to UPDATED_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.greaterThanOrEqual=" + UPDATED_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl is less than or equal to DEFAULT_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.lessThanOrEqual=" + DEFAULT_NUM_BL);

        // Get all the bonReceptionList where numBl is less than or equal to SMALLER_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.lessThanOrEqual=" + SMALLER_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsLessThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl is less than DEFAULT_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.lessThan=" + DEFAULT_NUM_BL);

        // Get all the bonReceptionList where numBl is less than UPDATED_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.lessThan=" + UPDATED_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByNumBlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where numBl is greater than DEFAULT_NUM_BL
        defaultBonReceptionShouldNotBeFound("numBl.greaterThan=" + DEFAULT_NUM_BL);

        // Get all the bonReceptionList where numBl is greater than SMALLER_NUM_BL
        defaultBonReceptionShouldBeFound("numBl.greaterThan=" + SMALLER_NUM_BL);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation equals to UPDATED_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation equals to UPDATED_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation is not null
        defaultBonReceptionShouldBeFound("dateCreation.specified=true");

        // Get all the bonReceptionList where dateCreation is null
        defaultBonReceptionShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation is less than UPDATED_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        // Get all the bonReceptionList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultBonReceptionShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the bonReceptionList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultBonReceptionShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBonReceptionsByBonReceptionItemIsEqualToSomething() throws Exception {
        BonReceptionItem bonReceptionItem;
        if (TestUtil.findAll(em, BonReceptionItem.class).isEmpty()) {
            bonReceptionRepository.saveAndFlush(bonReception);
            bonReceptionItem = BonReceptionItemResourceIT.createEntity(em);
        } else {
            bonReceptionItem = TestUtil.findAll(em, BonReceptionItem.class).get(0);
        }
        em.persist(bonReceptionItem);
        em.flush();
        bonReception.addBonReceptionItem(bonReceptionItem);
        bonReceptionRepository.saveAndFlush(bonReception);
        Long bonReceptionItemId = bonReceptionItem.getId();

        // Get all the bonReceptionList where bonReceptionItem equals to bonReceptionItemId
        defaultBonReceptionShouldBeFound("bonReceptionItemId.equals=" + bonReceptionItemId);

        // Get all the bonReceptionList where bonReceptionItem equals to (bonReceptionItemId + 1)
        defaultBonReceptionShouldNotBeFound("bonReceptionItemId.equals=" + (bonReceptionItemId + 1));
    }

    @Test
    @Transactional
    void getAllBonReceptionsByFournisseurIsEqualToSomething() throws Exception {
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            bonReceptionRepository.saveAndFlush(bonReception);
            fournisseur = FournisseurResourceIT.createEntity(em);
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        em.persist(fournisseur);
        em.flush();
        bonReception.setFournisseur(fournisseur);
        bonReceptionRepository.saveAndFlush(bonReception);
        Long fournisseurId = fournisseur.getId();

        // Get all the bonReceptionList where fournisseur equals to fournisseurId
        defaultBonReceptionShouldBeFound("fournisseurId.equals=" + fournisseurId);

        // Get all the bonReceptionList where fournisseur equals to (fournisseurId + 1)
        defaultBonReceptionShouldNotBeFound("fournisseurId.equals=" + (fournisseurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBonReceptionShouldBeFound(String filter) throws Exception {
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonReception.getId().intValue())))
            .andExpect(jsonPath("$.[*].informaton").value(hasItem(DEFAULT_INFORMATON)))
            .andExpect(jsonPath("$.[*].numFacture").value(hasItem(DEFAULT_NUM_FACTURE)))
            .andExpect(jsonPath("$.[*].numBl").value(hasItem(DEFAULT_NUM_BL)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBonReceptionShouldNotBeFound(String filter) throws Exception {
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBonReceptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBonReception() throws Exception {
        // Get the bonReception
        restBonReceptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBonReception() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();

        // Update the bonReception
        BonReception updatedBonReception = bonReceptionRepository.findById(bonReception.getId()).get();
        // Disconnect from session so that the updates on updatedBonReception are not directly saved in db
        em.detach(updatedBonReception);
        updatedBonReception
            .informaton(UPDATED_INFORMATON)
            .numFacture(UPDATED_NUM_FACTURE)
            .numBl(UPDATED_NUM_BL)
            .dateCreation(UPDATED_DATE_CREATION);
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(updatedBonReception);

        restBonReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonReceptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
        BonReception testBonReception = bonReceptionList.get(bonReceptionList.size() - 1);
        assertThat(testBonReception.getInformaton()).isEqualTo(UPDATED_INFORMATON);
        assertThat(testBonReception.getNumFacture()).isEqualTo(UPDATED_NUM_FACTURE);
        assertThat(testBonReception.getNumBl()).isEqualTo(UPDATED_NUM_BL);
        assertThat(testBonReception.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonReceptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonReceptionWithPatch() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();

        // Update the bonReception using partial update
        BonReception partialUpdatedBonReception = new BonReception();
        partialUpdatedBonReception.setId(bonReception.getId());

        partialUpdatedBonReception.informaton(UPDATED_INFORMATON).numFacture(UPDATED_NUM_FACTURE).dateCreation(UPDATED_DATE_CREATION);

        restBonReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonReception))
            )
            .andExpect(status().isOk());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
        BonReception testBonReception = bonReceptionList.get(bonReceptionList.size() - 1);
        assertThat(testBonReception.getInformaton()).isEqualTo(UPDATED_INFORMATON);
        assertThat(testBonReception.getNumFacture()).isEqualTo(UPDATED_NUM_FACTURE);
        assertThat(testBonReception.getNumBl()).isEqualTo(DEFAULT_NUM_BL);
        assertThat(testBonReception.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateBonReceptionWithPatch() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();

        // Update the bonReception using partial update
        BonReception partialUpdatedBonReception = new BonReception();
        partialUpdatedBonReception.setId(bonReception.getId());

        partialUpdatedBonReception
            .informaton(UPDATED_INFORMATON)
            .numFacture(UPDATED_NUM_FACTURE)
            .numBl(UPDATED_NUM_BL)
            .dateCreation(UPDATED_DATE_CREATION);

        restBonReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonReception))
            )
            .andExpect(status().isOk());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
        BonReception testBonReception = bonReceptionList.get(bonReceptionList.size() - 1);
        assertThat(testBonReception.getInformaton()).isEqualTo(UPDATED_INFORMATON);
        assertThat(testBonReception.getNumFacture()).isEqualTo(UPDATED_NUM_FACTURE);
        assertThat(testBonReception.getNumBl()).isEqualTo(UPDATED_NUM_BL);
        assertThat(testBonReception.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bonReceptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBonReception() throws Exception {
        int databaseSizeBeforeUpdate = bonReceptionRepository.findAll().size();
        bonReception.setId(count.incrementAndGet());

        // Create the BonReception
        BonReceptionDTO bonReceptionDTO = bonReceptionMapper.toDto(bonReception);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonReceptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonReception in the database
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBonReception() throws Exception {
        // Initialize the database
        bonReceptionRepository.saveAndFlush(bonReception);

        int databaseSizeBeforeDelete = bonReceptionRepository.findAll().size();

        // Delete the bonReception
        restBonReceptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, bonReception.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BonReception> bonReceptionList = bonReceptionRepository.findAll();
        assertThat(bonReceptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
