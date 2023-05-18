package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Order;
import com.jbd.stock.domain.Paiement;
import com.jbd.stock.domain.Role;
import com.jbd.stock.domain.User;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.domain.enumeration.UserType;
import com.jbd.stock.repository.UtilisateurRepository;
import com.jbd.stock.service.UtilisateurService;
import com.jbd.stock.service.criteria.UtilisateurCriteria;
import com.jbd.stock.service.dto.UtilisateurDTO;
import com.jbd.stock.service.mapper.UtilisateurMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION = "BBBBBBBBBB";

    private static final UserType DEFAULT_TYPE = UserType.CLIENT;
    private static final UserType UPDATED_TYPE = UserType.USER;

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepositoryMock;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Mock
    private UtilisateurService utilisateurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateCreation(DEFAULT_DATE_CREATION)
            .status(DEFAULT_STATUS)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .information(DEFAULT_INFORMATION)
            .type(DEFAULT_TYPE);
        return utilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .status(UPDATED_STATUS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .information(UPDATED_INFORMATION)
            .type(UPDATED_TYPE);
        return utilisateur;
    }

    @BeforeEach
    public void initTest() {
        utilisateur = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();
        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);
        restUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testUtilisateur.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUtilisateur.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUtilisateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUtilisateur.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testUtilisateur.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilisateurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(utilisateurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getUtilisateursByIdFiltering() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        Long id = utilisateur.getId();

        defaultUtilisateurShouldBeFound("id.equals=" + id);
        defaultUtilisateurShouldNotBeFound("id.notEquals=" + id);

        defaultUtilisateurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUtilisateurShouldNotBeFound("id.greaterThan=" + id);

        defaultUtilisateurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUtilisateurShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUtilisateursByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where code equals to DEFAULT_CODE
        defaultUtilisateurShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the utilisateurList where code equals to UPDATED_CODE
        defaultUtilisateurShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where code in DEFAULT_CODE or UPDATED_CODE
        defaultUtilisateurShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the utilisateurList where code equals to UPDATED_CODE
        defaultUtilisateurShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where code is not null
        defaultUtilisateurShouldBeFound("code.specified=true");

        // Get all the utilisateurList where code is null
        defaultUtilisateurShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByCodeContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where code contains DEFAULT_CODE
        defaultUtilisateurShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the utilisateurList where code contains UPDATED_CODE
        defaultUtilisateurShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where code does not contain DEFAULT_CODE
        defaultUtilisateurShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the utilisateurList where code does not contain UPDATED_CODE
        defaultUtilisateurShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom equals to DEFAULT_NOM
        defaultUtilisateurShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the utilisateurList where nom equals to UPDATED_NOM
        defaultUtilisateurShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultUtilisateurShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the utilisateurList where nom equals to UPDATED_NOM
        defaultUtilisateurShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom is not null
        defaultUtilisateurShouldBeFound("nom.specified=true");

        // Get all the utilisateurList where nom is null
        defaultUtilisateurShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom contains DEFAULT_NOM
        defaultUtilisateurShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the utilisateurList where nom contains UPDATED_NOM
        defaultUtilisateurShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom does not contain DEFAULT_NOM
        defaultUtilisateurShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the utilisateurList where nom does not contain UPDATED_NOM
        defaultUtilisateurShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom equals to DEFAULT_PRENOM
        defaultUtilisateurShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the utilisateurList where prenom equals to UPDATED_PRENOM
        defaultUtilisateurShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultUtilisateurShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the utilisateurList where prenom equals to UPDATED_PRENOM
        defaultUtilisateurShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom is not null
        defaultUtilisateurShouldBeFound("prenom.specified=true");

        // Get all the utilisateurList where prenom is null
        defaultUtilisateurShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom contains DEFAULT_PRENOM
        defaultUtilisateurShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the utilisateurList where prenom contains UPDATED_PRENOM
        defaultUtilisateurShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom does not contain DEFAULT_PRENOM
        defaultUtilisateurShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the utilisateurList where prenom does not contain UPDATED_PRENOM
        defaultUtilisateurShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the utilisateurList where dateCreation equals to UPDATED_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the utilisateurList where dateCreation equals to UPDATED_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is not null
        defaultUtilisateurShouldBeFound("dateCreation.specified=true");

        // Get all the utilisateurList where dateCreation is null
        defaultUtilisateurShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the utilisateurList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the utilisateurList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the utilisateurList where dateCreation is less than UPDATED_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultUtilisateurShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the utilisateurList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultUtilisateurShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where status equals to DEFAULT_STATUS
        defaultUtilisateurShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the utilisateurList where status equals to UPDATED_STATUS
        defaultUtilisateurShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUtilisateurShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the utilisateurList where status equals to UPDATED_STATUS
        defaultUtilisateurShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where status is not null
        defaultUtilisateurShouldBeFound("status.specified=true");

        // Get all the utilisateurList where status is null
        defaultUtilisateurShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByStatusContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where status contains DEFAULT_STATUS
        defaultUtilisateurShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the utilisateurList where status contains UPDATED_STATUS
        defaultUtilisateurShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where status does not contain DEFAULT_STATUS
        defaultUtilisateurShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the utilisateurList where status does not contain UPDATED_STATUS
        defaultUtilisateurShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where phone equals to DEFAULT_PHONE
        defaultUtilisateurShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the utilisateurList where phone equals to UPDATED_PHONE
        defaultUtilisateurShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUtilisateurShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the utilisateurList where phone equals to UPDATED_PHONE
        defaultUtilisateurShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where phone is not null
        defaultUtilisateurShouldBeFound("phone.specified=true");

        // Get all the utilisateurList where phone is null
        defaultUtilisateurShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByPhoneContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where phone contains DEFAULT_PHONE
        defaultUtilisateurShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the utilisateurList where phone contains UPDATED_PHONE
        defaultUtilisateurShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where phone does not contain DEFAULT_PHONE
        defaultUtilisateurShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the utilisateurList where phone does not contain UPDATED_PHONE
        defaultUtilisateurShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email equals to DEFAULT_EMAIL
        defaultUtilisateurShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the utilisateurList where email equals to UPDATED_EMAIL
        defaultUtilisateurShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUtilisateurShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the utilisateurList where email equals to UPDATED_EMAIL
        defaultUtilisateurShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email is not null
        defaultUtilisateurShouldBeFound("email.specified=true");

        // Get all the utilisateurList where email is null
        defaultUtilisateurShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email contains DEFAULT_EMAIL
        defaultUtilisateurShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the utilisateurList where email contains UPDATED_EMAIL
        defaultUtilisateurShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email does not contain DEFAULT_EMAIL
        defaultUtilisateurShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the utilisateurList where email does not contain UPDATED_EMAIL
        defaultUtilisateurShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where information equals to DEFAULT_INFORMATION
        defaultUtilisateurShouldBeFound("information.equals=" + DEFAULT_INFORMATION);

        // Get all the utilisateurList where information equals to UPDATED_INFORMATION
        defaultUtilisateurShouldNotBeFound("information.equals=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByInformationIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where information in DEFAULT_INFORMATION or UPDATED_INFORMATION
        defaultUtilisateurShouldBeFound("information.in=" + DEFAULT_INFORMATION + "," + UPDATED_INFORMATION);

        // Get all the utilisateurList where information equals to UPDATED_INFORMATION
        defaultUtilisateurShouldNotBeFound("information.in=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where information is not null
        defaultUtilisateurShouldBeFound("information.specified=true");

        // Get all the utilisateurList where information is null
        defaultUtilisateurShouldNotBeFound("information.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByInformationContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where information contains DEFAULT_INFORMATION
        defaultUtilisateurShouldBeFound("information.contains=" + DEFAULT_INFORMATION);

        // Get all the utilisateurList where information contains UPDATED_INFORMATION
        defaultUtilisateurShouldNotBeFound("information.contains=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByInformationNotContainsSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where information does not contain DEFAULT_INFORMATION
        defaultUtilisateurShouldNotBeFound("information.doesNotContain=" + DEFAULT_INFORMATION);

        // Get all the utilisateurList where information does not contain UPDATED_INFORMATION
        defaultUtilisateurShouldBeFound("information.doesNotContain=" + UPDATED_INFORMATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where type equals to DEFAULT_TYPE
        defaultUtilisateurShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the utilisateurList where type equals to UPDATED_TYPE
        defaultUtilisateurShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultUtilisateurShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the utilisateurList where type equals to UPDATED_TYPE
        defaultUtilisateurShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where type is not null
        defaultUtilisateurShouldBeFound("type.specified=true");

        // Get all the utilisateurList where type is null
        defaultUtilisateurShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        utilisateur.setUser(user);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long userId = user.getId();

        // Get all the utilisateurList where user equals to userId
        defaultUtilisateurShouldBeFound("userId.equals=" + userId);

        // Get all the utilisateurList where user equals to (userId + 1)
        defaultUtilisateurShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUtilisateursByPaiementIsEqualToSomething() throws Exception {
        Paiement paiement;
        if (TestUtil.findAll(em, Paiement.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            paiement = PaiementResourceIT.createEntity(em);
        } else {
            paiement = TestUtil.findAll(em, Paiement.class).get(0);
        }
        em.persist(paiement);
        em.flush();
        utilisateur.addPaiement(paiement);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long paiementId = paiement.getId();

        // Get all the utilisateurList where paiement equals to paiementId
        defaultUtilisateurShouldBeFound("paiementId.equals=" + paiementId);

        // Get all the utilisateurList where paiement equals to (paiementId + 1)
        defaultUtilisateurShouldNotBeFound("paiementId.equals=" + (paiementId + 1));
    }

    @Test
    @Transactional
    void getAllUtilisateursByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        utilisateur.addOrder(order);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long orderId = order.getId();

        // Get all the utilisateurList where order equals to orderId
        defaultUtilisateurShouldBeFound("orderId.equals=" + orderId);

        // Get all the utilisateurList where order equals to (orderId + 1)
        defaultUtilisateurShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllUtilisateursByRolesIsEqualToSomething() throws Exception {
        Role roles;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            roles = RoleResourceIT.createEntity(em);
        } else {
            roles = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(roles);
        em.flush();
        utilisateur.addRoles(roles);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long rolesId = roles.getId();

        // Get all the utilisateurList where roles equals to rolesId
        defaultUtilisateurShouldBeFound("rolesId.equals=" + rolesId);

        // Get all the utilisateurList where roles equals to (rolesId + 1)
        defaultUtilisateurShouldNotBeFound("rolesId.equals=" + (rolesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUtilisateurShouldBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUtilisateurShouldNotBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
        // Disconnect from session so that the updates on updatedUtilisateur are not directly saved in db
        em.detach(updatedUtilisateur);
        updatedUtilisateur
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .status(UPDATED_STATUS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .information(UPDATED_INFORMATION)
            .type(UPDATED_TYPE);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(updatedUtilisateur);

        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testUtilisateur.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUtilisateur.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testUtilisateur.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .code(UPDATED_CODE)
            .dateCreation(UPDATED_DATE_CREATION)
            .email(UPDATED_EMAIL)
            .information(UPDATED_INFORMATION);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testUtilisateur.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUtilisateur.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testUtilisateur.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .status(UPDATED_STATUS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .information(UPDATED_INFORMATION)
            .type(UPDATED_TYPE);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testUtilisateur.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUtilisateur.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testUtilisateur.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Delete the utilisateur
        restUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
