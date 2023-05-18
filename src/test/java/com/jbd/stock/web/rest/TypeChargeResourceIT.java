package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Charge;
import com.jbd.stock.domain.TypeCharge;
import com.jbd.stock.repository.TypeChargeRepository;
import com.jbd.stock.service.criteria.TypeChargeCriteria;
import com.jbd.stock.service.dto.TypeChargeDTO;
import com.jbd.stock.service.mapper.TypeChargeMapper;
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
 * Integration tests for the {@link TypeChargeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeChargeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeChargeRepository typeChargeRepository;

    @Autowired
    private TypeChargeMapper typeChargeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeChargeMockMvc;

    private TypeCharge typeCharge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeCharge createEntity(EntityManager em) {
        TypeCharge typeCharge = new TypeCharge().nom(DEFAULT_NOM);
        return typeCharge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeCharge createUpdatedEntity(EntityManager em) {
        TypeCharge typeCharge = new TypeCharge().nom(UPDATED_NOM);
        return typeCharge;
    }

    @BeforeEach
    public void initTest() {
        typeCharge = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeCharge() throws Exception {
        int databaseSizeBeforeCreate = typeChargeRepository.findAll().size();
        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);
        restTypeChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeCreate + 1);
        TypeCharge testTypeCharge = typeChargeList.get(typeChargeList.size() - 1);
        assertThat(testTypeCharge.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createTypeChargeWithExistingId() throws Exception {
        // Create the TypeCharge with an existing ID
        typeCharge.setId(1L);
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        int databaseSizeBeforeCreate = typeChargeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeChargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeCharges() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getTypeCharge() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get the typeCharge
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL_ID, typeCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeCharge.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getTypeChargesByIdFiltering() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        Long id = typeCharge.getId();

        defaultTypeChargeShouldBeFound("id.equals=" + id);
        defaultTypeChargeShouldNotBeFound("id.notEquals=" + id);

        defaultTypeChargeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeChargeShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeChargeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeChargeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeChargesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList where nom equals to DEFAULT_NOM
        defaultTypeChargeShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the typeChargeList where nom equals to UPDATED_NOM
        defaultTypeChargeShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeChargesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultTypeChargeShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the typeChargeList where nom equals to UPDATED_NOM
        defaultTypeChargeShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeChargesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList where nom is not null
        defaultTypeChargeShouldBeFound("nom.specified=true");

        // Get all the typeChargeList where nom is null
        defaultTypeChargeShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeChargesByNomContainsSomething() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList where nom contains DEFAULT_NOM
        defaultTypeChargeShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the typeChargeList where nom contains UPDATED_NOM
        defaultTypeChargeShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeChargesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        // Get all the typeChargeList where nom does not contain DEFAULT_NOM
        defaultTypeChargeShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the typeChargeList where nom does not contain UPDATED_NOM
        defaultTypeChargeShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeChargesByChargeIsEqualToSomething() throws Exception {
        Charge charge;
        if (TestUtil.findAll(em, Charge.class).isEmpty()) {
            typeChargeRepository.saveAndFlush(typeCharge);
            charge = ChargeResourceIT.createEntity(em);
        } else {
            charge = TestUtil.findAll(em, Charge.class).get(0);
        }
        em.persist(charge);
        em.flush();
        typeCharge.addCharge(charge);
        typeChargeRepository.saveAndFlush(typeCharge);
        Long chargeId = charge.getId();

        // Get all the typeChargeList where charge equals to chargeId
        defaultTypeChargeShouldBeFound("chargeId.equals=" + chargeId);

        // Get all the typeChargeList where charge equals to (chargeId + 1)
        defaultTypeChargeShouldNotBeFound("chargeId.equals=" + (chargeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeChargeShouldBeFound(String filter) throws Exception {
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeChargeShouldNotBeFound(String filter) throws Exception {
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeChargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeCharge() throws Exception {
        // Get the typeCharge
        restTypeChargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeCharge() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();

        // Update the typeCharge
        TypeCharge updatedTypeCharge = typeChargeRepository.findById(typeCharge.getId()).get();
        // Disconnect from session so that the updates on updatedTypeCharge are not directly saved in db
        em.detach(updatedTypeCharge);
        updatedTypeCharge.nom(UPDATED_NOM);
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(updatedTypeCharge);

        restTypeChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeChargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
        TypeCharge testTypeCharge = typeChargeList.get(typeChargeList.size() - 1);
        assertThat(testTypeCharge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeChargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeChargeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeChargeWithPatch() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();

        // Update the typeCharge using partial update
        TypeCharge partialUpdatedTypeCharge = new TypeCharge();
        partialUpdatedTypeCharge.setId(typeCharge.getId());

        partialUpdatedTypeCharge.nom(UPDATED_NOM);

        restTypeChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeCharge))
            )
            .andExpect(status().isOk());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
        TypeCharge testTypeCharge = typeChargeList.get(typeChargeList.size() - 1);
        assertThat(testTypeCharge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateTypeChargeWithPatch() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();

        // Update the typeCharge using partial update
        TypeCharge partialUpdatedTypeCharge = new TypeCharge();
        partialUpdatedTypeCharge.setId(typeCharge.getId());

        partialUpdatedTypeCharge.nom(UPDATED_NOM);

        restTypeChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeCharge))
            )
            .andExpect(status().isOk());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
        TypeCharge testTypeCharge = typeChargeList.get(typeChargeList.size() - 1);
        assertThat(testTypeCharge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeChargeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeCharge() throws Exception {
        int databaseSizeBeforeUpdate = typeChargeRepository.findAll().size();
        typeCharge.setId(count.incrementAndGet());

        // Create the TypeCharge
        TypeChargeDTO typeChargeDTO = typeChargeMapper.toDto(typeCharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeChargeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeChargeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeCharge in the database
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeCharge() throws Exception {
        // Initialize the database
        typeChargeRepository.saveAndFlush(typeCharge);

        int databaseSizeBeforeDelete = typeChargeRepository.findAll().size();

        // Delete the typeCharge
        restTypeChargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeCharge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeCharge> typeChargeList = typeChargeRepository.findAll();
        assertThat(typeChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
