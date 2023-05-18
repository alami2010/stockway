package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.BonReception;
import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.repository.FournisseurRepository;
import com.jbd.stock.service.criteria.FournisseurCriteria;
import com.jbd.stock.service.dto.FournisseurDTO;
import com.jbd.stock.service.mapper.FournisseurMapper;
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
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FournisseurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fournisseurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurMapper fournisseurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .code(DEFAULT_CODE)
            .ville(DEFAULT_VILLE)
            .adresse(DEFAULT_ADRESSE)
            .activite(DEFAULT_ACTIVITE)
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION);
        return fournisseur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .code(UPDATED_CODE)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .activite(UPDATED_ACTIVITE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION);
        return fournisseur;
    }

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();
        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFournisseur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFournisseur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFournisseur.getActivite()).isEqualTo(DEFAULT_ACTIVITE);
        assertThat(testFournisseur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFournisseur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFournisseurWithExistingId() throws Exception {
        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL_ID, fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.activite").value(DEFAULT_ACTIVITE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFournisseursByIdFiltering() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        Long id = fournisseur.getId();

        defaultFournisseurShouldBeFound("id.equals=" + id);
        defaultFournisseurShouldNotBeFound("id.notEquals=" + id);

        defaultFournisseurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.greaterThan=" + id);

        defaultFournisseurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFournisseursByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where code equals to DEFAULT_CODE
        defaultFournisseurShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the fournisseurList where code equals to UPDATED_CODE
        defaultFournisseurShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFournisseursByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where code in DEFAULT_CODE or UPDATED_CODE
        defaultFournisseurShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the fournisseurList where code equals to UPDATED_CODE
        defaultFournisseurShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFournisseursByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where code is not null
        defaultFournisseurShouldBeFound("code.specified=true");

        // Get all the fournisseurList where code is null
        defaultFournisseurShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByCodeContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where code contains DEFAULT_CODE
        defaultFournisseurShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the fournisseurList where code contains UPDATED_CODE
        defaultFournisseurShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFournisseursByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where code does not contain DEFAULT_CODE
        defaultFournisseurShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the fournisseurList where code does not contain UPDATED_CODE
        defaultFournisseurShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville equals to DEFAULT_VILLE
        defaultFournisseurShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville equals to UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultFournisseurShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the fournisseurList where ville equals to UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville is not null
        defaultFournisseurShouldBeFound("ville.specified=true");

        // Get all the fournisseurList where ville is null
        defaultFournisseurShouldNotBeFound("ville.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville contains DEFAULT_VILLE
        defaultFournisseurShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville contains UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville does not contain DEFAULT_VILLE
        defaultFournisseurShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville does not contain UPDATED_VILLE
        defaultFournisseurShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse equals to DEFAULT_ADRESSE
        defaultFournisseurShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the fournisseurList where adresse equals to UPDATED_ADRESSE
        defaultFournisseurShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultFournisseurShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the fournisseurList where adresse equals to UPDATED_ADRESSE
        defaultFournisseurShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse is not null
        defaultFournisseurShouldBeFound("adresse.specified=true");

        // Get all the fournisseurList where adresse is null
        defaultFournisseurShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse contains DEFAULT_ADRESSE
        defaultFournisseurShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the fournisseurList where adresse contains UPDATED_ADRESSE
        defaultFournisseurShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse does not contain DEFAULT_ADRESSE
        defaultFournisseurShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the fournisseurList where adresse does not contain UPDATED_ADRESSE
        defaultFournisseurShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where activite equals to DEFAULT_ACTIVITE
        defaultFournisseurShouldBeFound("activite.equals=" + DEFAULT_ACTIVITE);

        // Get all the fournisseurList where activite equals to UPDATED_ACTIVITE
        defaultFournisseurShouldNotBeFound("activite.equals=" + UPDATED_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllFournisseursByActiviteIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where activite in DEFAULT_ACTIVITE or UPDATED_ACTIVITE
        defaultFournisseurShouldBeFound("activite.in=" + DEFAULT_ACTIVITE + "," + UPDATED_ACTIVITE);

        // Get all the fournisseurList where activite equals to UPDATED_ACTIVITE
        defaultFournisseurShouldNotBeFound("activite.in=" + UPDATED_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllFournisseursByActiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where activite is not null
        defaultFournisseurShouldBeFound("activite.specified=true");

        // Get all the fournisseurList where activite is null
        defaultFournisseurShouldNotBeFound("activite.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByActiviteContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where activite contains DEFAULT_ACTIVITE
        defaultFournisseurShouldBeFound("activite.contains=" + DEFAULT_ACTIVITE);

        // Get all the fournisseurList where activite contains UPDATED_ACTIVITE
        defaultFournisseurShouldNotBeFound("activite.contains=" + UPDATED_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllFournisseursByActiviteNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where activite does not contain DEFAULT_ACTIVITE
        defaultFournisseurShouldNotBeFound("activite.doesNotContain=" + DEFAULT_ACTIVITE);

        // Get all the fournisseurList where activite does not contain UPDATED_ACTIVITE
        defaultFournisseurShouldBeFound("activite.doesNotContain=" + UPDATED_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom equals to DEFAULT_NOM
        defaultFournisseurShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom equals to UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultFournisseurShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the fournisseurList where nom equals to UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom is not null
        defaultFournisseurShouldBeFound("nom.specified=true");

        // Get all the fournisseurList where nom is null
        defaultFournisseurShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByNomContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom contains DEFAULT_NOM
        defaultFournisseurShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom contains UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom does not contain DEFAULT_NOM
        defaultFournisseurShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom does not contain UPDATED_NOM
        defaultFournisseurShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where description equals to DEFAULT_DESCRIPTION
        defaultFournisseurShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fournisseurList where description equals to UPDATED_DESCRIPTION
        defaultFournisseurShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFournisseursByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFournisseurShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fournisseurList where description equals to UPDATED_DESCRIPTION
        defaultFournisseurShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFournisseursByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where description is not null
        defaultFournisseurShouldBeFound("description.specified=true");

        // Get all the fournisseurList where description is null
        defaultFournisseurShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where description contains DEFAULT_DESCRIPTION
        defaultFournisseurShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fournisseurList where description contains UPDATED_DESCRIPTION
        defaultFournisseurShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFournisseursByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where description does not contain DEFAULT_DESCRIPTION
        defaultFournisseurShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fournisseurList where description does not contain UPDATED_DESCRIPTION
        defaultFournisseurShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFournisseursByBonReceptionIsEqualToSomething() throws Exception {
        BonReception bonReception;
        if (TestUtil.findAll(em, BonReception.class).isEmpty()) {
            fournisseurRepository.saveAndFlush(fournisseur);
            bonReception = BonReceptionResourceIT.createEntity(em);
        } else {
            bonReception = TestUtil.findAll(em, BonReception.class).get(0);
        }
        em.persist(bonReception);
        em.flush();
        fournisseur.addBonReception(bonReception);
        fournisseurRepository.saveAndFlush(fournisseur);
        Long bonReceptionId = bonReception.getId();

        // Get all the fournisseurList where bonReception equals to bonReceptionId
        defaultFournisseurShouldBeFound("bonReceptionId.equals=" + bonReceptionId);

        // Get all the fournisseurList where bonReception equals to (bonReceptionId + 1)
        defaultFournisseurShouldNotBeFound("bonReceptionId.equals=" + (bonReceptionId + 1));
    }

    @Test
    @Transactional
    void getAllFournisseursByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            fournisseurRepository.saveAndFlush(fournisseur);
            article = ArticleResourceIT.createEntity(em);
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        fournisseur.addArticle(article);
        fournisseurRepository.saveAndFlush(fournisseur);
        Long articleId = article.getId();

        // Get all the fournisseurList where article equals to articleId
        defaultFournisseurShouldBeFound("articleId.equals=" + articleId);

        // Get all the fournisseurList where article equals to (articleId + 1)
        defaultFournisseurShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFournisseurShouldBeFound(String filter) throws Exception {
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFournisseurShouldNotBeFound(String filter) throws Exception {
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
        updatedFournisseur
            .code(UPDATED_CODE)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .activite(UPDATED_ACTIVITE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(updatedFournisseur);

        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testFournisseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFournisseur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur.ville(UPDATED_VILLE).adresse(UPDATED_ADRESSE).activite(UPDATED_ACTIVITE);

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testFournisseur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFournisseur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .code(UPDATED_CODE)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .activite(UPDATED_ACTIVITE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION);

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFournisseur.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testFournisseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFournisseur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Delete the fournisseur
        restFournisseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, fournisseur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
