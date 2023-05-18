package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.BonReceptionItem;
import com.jbd.stock.domain.Category;
import com.jbd.stock.domain.Fournisseur;
import com.jbd.stock.domain.OrderItem;
import com.jbd.stock.repository.ArticleRepository;
import com.jbd.stock.service.criteria.ArticleCriteria;
import com.jbd.stock.service.dto.ArticleDTO;
import com.jbd.stock.service.mapper.ArticleMapper;
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
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_ACHAT = 1D;
    private static final Double UPDATED_PRIX_ACHAT = 2D;
    private static final Double SMALLER_PRIX_ACHAT = 1D - 1D;

    private static final Double DEFAULT_QTE = 1D;
    private static final Double UPDATED_QTE = 2D;
    private static final Double SMALLER_QTE = 1D - 1D;

    private static final Double DEFAULT_QTE_ALERT = 1D;
    private static final Double UPDATED_QTE_ALERT = 2D;
    private static final Double SMALLER_QTE_ALERT = 1D - 1D;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .prixAchat(DEFAULT_PRIX_ACHAT)
            .qte(DEFAULT_QTE)
            .qteAlert(DEFAULT_QTE_ALERT)
            .status(DEFAULT_STATUS)
            .dateCreation(DEFAULT_DATE_CREATION);
        return article;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .qte(UPDATED_QTE)
            .qteAlert(UPDATED_QTE_ALERT)
            .status(UPDATED_STATUS)
            .dateCreation(UPDATED_DATE_CREATION);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();
        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticle.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArticle.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);
        assertThat(testArticle.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testArticle.getQteAlert()).isEqualTo(DEFAULT_QTE_ALERT);
        assertThat(testArticle.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testArticle.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createArticleWithExistingId() throws Exception {
        // Create the Article with an existing ID
        article.setId(1L);
        ArticleDTO articleDTO = articleMapper.toDto(article);

        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE.doubleValue())))
            .andExpect(jsonPath("$.[*].qteAlert").value(hasItem(DEFAULT_QTE_ALERT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.prixAchat").value(DEFAULT_PRIX_ACHAT.doubleValue()))
            .andExpect(jsonPath("$.qte").value(DEFAULT_QTE.doubleValue()))
            .andExpect(jsonPath("$.qteAlert").value(DEFAULT_QTE_ALERT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getArticlesByIdFiltering() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        Long id = article.getId();

        defaultArticleShouldBeFound("id.equals=" + id);
        defaultArticleShouldNotBeFound("id.notEquals=" + id);

        defaultArticleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.greaterThan=" + id);

        defaultArticleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where code equals to DEFAULT_CODE
        defaultArticleShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the articleList where code equals to UPDATED_CODE
        defaultArticleShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where code in DEFAULT_CODE or UPDATED_CODE
        defaultArticleShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the articleList where code equals to UPDATED_CODE
        defaultArticleShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where code is not null
        defaultArticleShouldBeFound("code.specified=true");

        // Get all the articleList where code is null
        defaultArticleShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByCodeContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where code contains DEFAULT_CODE
        defaultArticleShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the articleList where code contains UPDATED_CODE
        defaultArticleShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where code does not contain DEFAULT_CODE
        defaultArticleShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the articleList where code does not contain UPDATED_CODE
        defaultArticleShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where nom equals to DEFAULT_NOM
        defaultArticleShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the articleList where nom equals to UPDATED_NOM
        defaultArticleShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArticlesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultArticleShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the articleList where nom equals to UPDATED_NOM
        defaultArticleShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArticlesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where nom is not null
        defaultArticleShouldBeFound("nom.specified=true");

        // Get all the articleList where nom is null
        defaultArticleShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByNomContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where nom contains DEFAULT_NOM
        defaultArticleShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the articleList where nom contains UPDATED_NOM
        defaultArticleShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArticlesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where nom does not contain DEFAULT_NOM
        defaultArticleShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the articleList where nom does not contain UPDATED_NOM
        defaultArticleShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description equals to DEFAULT_DESCRIPTION
        defaultArticleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description equals to UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultArticleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the articleList where description equals to UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description is not null
        defaultArticleShouldBeFound("description.specified=true");

        // Get all the articleList where description is null
        defaultArticleShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description contains DEFAULT_DESCRIPTION
        defaultArticleShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description contains UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description does not contain DEFAULT_DESCRIPTION
        defaultArticleShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description does not contain UPDATED_DESCRIPTION
        defaultArticleShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat equals to DEFAULT_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.equals=" + DEFAULT_PRIX_ACHAT);

        // Get all the articleList where prixAchat equals to UPDATED_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.equals=" + UPDATED_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat in DEFAULT_PRIX_ACHAT or UPDATED_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.in=" + DEFAULT_PRIX_ACHAT + "," + UPDATED_PRIX_ACHAT);

        // Get all the articleList where prixAchat equals to UPDATED_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.in=" + UPDATED_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat is not null
        defaultArticleShouldBeFound("prixAchat.specified=true");

        // Get all the articleList where prixAchat is null
        defaultArticleShouldNotBeFound("prixAchat.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat is greater than or equal to DEFAULT_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.greaterThanOrEqual=" + DEFAULT_PRIX_ACHAT);

        // Get all the articleList where prixAchat is greater than or equal to UPDATED_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.greaterThanOrEqual=" + UPDATED_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat is less than or equal to DEFAULT_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.lessThanOrEqual=" + DEFAULT_PRIX_ACHAT);

        // Get all the articleList where prixAchat is less than or equal to SMALLER_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.lessThanOrEqual=" + SMALLER_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat is less than DEFAULT_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.lessThan=" + DEFAULT_PRIX_ACHAT);

        // Get all the articleList where prixAchat is less than UPDATED_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.lessThan=" + UPDATED_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByPrixAchatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where prixAchat is greater than DEFAULT_PRIX_ACHAT
        defaultArticleShouldNotBeFound("prixAchat.greaterThan=" + DEFAULT_PRIX_ACHAT);

        // Get all the articleList where prixAchat is greater than SMALLER_PRIX_ACHAT
        defaultArticleShouldBeFound("prixAchat.greaterThan=" + SMALLER_PRIX_ACHAT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte equals to DEFAULT_QTE
        defaultArticleShouldBeFound("qte.equals=" + DEFAULT_QTE);

        // Get all the articleList where qte equals to UPDATED_QTE
        defaultArticleShouldNotBeFound("qte.equals=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte in DEFAULT_QTE or UPDATED_QTE
        defaultArticleShouldBeFound("qte.in=" + DEFAULT_QTE + "," + UPDATED_QTE);

        // Get all the articleList where qte equals to UPDATED_QTE
        defaultArticleShouldNotBeFound("qte.in=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte is not null
        defaultArticleShouldBeFound("qte.specified=true");

        // Get all the articleList where qte is null
        defaultArticleShouldNotBeFound("qte.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte is greater than or equal to DEFAULT_QTE
        defaultArticleShouldBeFound("qte.greaterThanOrEqual=" + DEFAULT_QTE);

        // Get all the articleList where qte is greater than or equal to UPDATED_QTE
        defaultArticleShouldNotBeFound("qte.greaterThanOrEqual=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte is less than or equal to DEFAULT_QTE
        defaultArticleShouldBeFound("qte.lessThanOrEqual=" + DEFAULT_QTE);

        // Get all the articleList where qte is less than or equal to SMALLER_QTE
        defaultArticleShouldNotBeFound("qte.lessThanOrEqual=" + SMALLER_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte is less than DEFAULT_QTE
        defaultArticleShouldNotBeFound("qte.lessThan=" + DEFAULT_QTE);

        // Get all the articleList where qte is less than UPDATED_QTE
        defaultArticleShouldBeFound("qte.lessThan=" + UPDATED_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qte is greater than DEFAULT_QTE
        defaultArticleShouldNotBeFound("qte.greaterThan=" + DEFAULT_QTE);

        // Get all the articleList where qte is greater than SMALLER_QTE
        defaultArticleShouldBeFound("qte.greaterThan=" + SMALLER_QTE);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert equals to DEFAULT_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.equals=" + DEFAULT_QTE_ALERT);

        // Get all the articleList where qteAlert equals to UPDATED_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.equals=" + UPDATED_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert in DEFAULT_QTE_ALERT or UPDATED_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.in=" + DEFAULT_QTE_ALERT + "," + UPDATED_QTE_ALERT);

        // Get all the articleList where qteAlert equals to UPDATED_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.in=" + UPDATED_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert is not null
        defaultArticleShouldBeFound("qteAlert.specified=true");

        // Get all the articleList where qteAlert is null
        defaultArticleShouldNotBeFound("qteAlert.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert is greater than or equal to DEFAULT_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.greaterThanOrEqual=" + DEFAULT_QTE_ALERT);

        // Get all the articleList where qteAlert is greater than or equal to UPDATED_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.greaterThanOrEqual=" + UPDATED_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert is less than or equal to DEFAULT_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.lessThanOrEqual=" + DEFAULT_QTE_ALERT);

        // Get all the articleList where qteAlert is less than or equal to SMALLER_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.lessThanOrEqual=" + SMALLER_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert is less than DEFAULT_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.lessThan=" + DEFAULT_QTE_ALERT);

        // Get all the articleList where qteAlert is less than UPDATED_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.lessThan=" + UPDATED_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByQteAlertIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where qteAlert is greater than DEFAULT_QTE_ALERT
        defaultArticleShouldNotBeFound("qteAlert.greaterThan=" + DEFAULT_QTE_ALERT);

        // Get all the articleList where qteAlert is greater than SMALLER_QTE_ALERT
        defaultArticleShouldBeFound("qteAlert.greaterThan=" + SMALLER_QTE_ALERT);
    }

    @Test
    @Transactional
    void getAllArticlesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where status equals to DEFAULT_STATUS
        defaultArticleShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the articleList where status equals to UPDATED_STATUS
        defaultArticleShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArticlesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultArticleShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the articleList where status equals to UPDATED_STATUS
        defaultArticleShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArticlesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where status is not null
        defaultArticleShouldBeFound("status.specified=true");

        // Get all the articleList where status is null
        defaultArticleShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByStatusContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where status contains DEFAULT_STATUS
        defaultArticleShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the articleList where status contains UPDATED_STATUS
        defaultArticleShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArticlesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where status does not contain DEFAULT_STATUS
        defaultArticleShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the articleList where status does not contain UPDATED_STATUS
        defaultArticleShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the articleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is not null
        defaultArticleShouldBeFound("dateCreation.specified=true");

        // Get all the articleList where dateCreation is null
        defaultArticleShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation is less than UPDATED_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllArticlesByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        em.persist(orderItem);
        em.flush();
        article.addOrderItem(orderItem);
        articleRepository.saveAndFlush(article);
        Long orderItemId = orderItem.getId();

        // Get all the articleList where orderItem equals to orderItemId
        defaultArticleShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the articleList where orderItem equals to (orderItemId + 1)
        defaultArticleShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllArticlesByBonReceptionItemIsEqualToSomething() throws Exception {
        BonReceptionItem bonReceptionItem;
        if (TestUtil.findAll(em, BonReceptionItem.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            bonReceptionItem = BonReceptionItemResourceIT.createEntity(em);
        } else {
            bonReceptionItem = TestUtil.findAll(em, BonReceptionItem.class).get(0);
        }
        em.persist(bonReceptionItem);
        em.flush();
        article.addBonReceptionItem(bonReceptionItem);
        articleRepository.saveAndFlush(article);
        Long bonReceptionItemId = bonReceptionItem.getId();

        // Get all the articleList where bonReceptionItem equals to bonReceptionItemId
        defaultArticleShouldBeFound("bonReceptionItemId.equals=" + bonReceptionItemId);

        // Get all the articleList where bonReceptionItem equals to (bonReceptionItemId + 1)
        defaultArticleShouldNotBeFound("bonReceptionItemId.equals=" + (bonReceptionItemId + 1));
    }

    @Test
    @Transactional
    void getAllArticlesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            category = CategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        article.setCategory(category);
        articleRepository.saveAndFlush(article);
        Long categoryId = category.getId();

        // Get all the articleList where category equals to categoryId
        defaultArticleShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the articleList where category equals to (categoryId + 1)
        defaultArticleShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllArticlesByFournisseurIsEqualToSomething() throws Exception {
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            fournisseur = FournisseurResourceIT.createEntity(em);
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        em.persist(fournisseur);
        em.flush();
        article.setFournisseur(fournisseur);
        articleRepository.saveAndFlush(article);
        Long fournisseurId = fournisseur.getId();

        // Get all the articleList where fournisseur equals to fournisseurId
        defaultArticleShouldBeFound("fournisseurId.equals=" + fournisseurId);

        // Get all the articleList where fournisseur equals to (fournisseurId + 1)
        defaultArticleShouldNotBeFound("fournisseurId.equals=" + (fournisseurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE.doubleValue())))
            .andExpect(jsonPath("$.[*].qteAlert").value(hasItem(DEFAULT_QTE_ALERT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .qte(UPDATED_QTE)
            .qteAlert(UPDATED_QTE_ALERT)
            .status(UPDATED_STATUS)
            .dateCreation(UPDATED_DATE_CREATION);
        ArticleDTO articleDTO = articleMapper.toDto(updatedArticle);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticle.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);
        assertThat(testArticle.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testArticle.getQteAlert()).isEqualTo(UPDATED_QTE_ALERT);
        assertThat(testArticle.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArticle.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle.code(UPDATED_CODE).nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).qteAlert(UPDATED_QTE_ALERT);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticle.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);
        assertThat(testArticle.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testArticle.getQteAlert()).isEqualTo(UPDATED_QTE_ALERT);
        assertThat(testArticle.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testArticle.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .qte(UPDATED_QTE)
            .qteAlert(UPDATED_QTE_ALERT)
            .status(UPDATED_STATUS)
            .dateCreation(UPDATED_DATE_CREATION);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticle.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);
        assertThat(testArticle.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testArticle.getQteAlert()).isEqualTo(UPDATED_QTE_ALERT);
        assertThat(testArticle.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArticle.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(count.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(articleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, article.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
