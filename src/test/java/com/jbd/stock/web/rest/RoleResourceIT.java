package com.jbd.stock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbd.stock.IntegrationTest;
import com.jbd.stock.domain.Role;
import com.jbd.stock.domain.Utilisateur;
import com.jbd.stock.repository.RoleRepository;
import com.jbd.stock.service.criteria.RoleCriteria;
import com.jbd.stock.service.dto.RoleDTO;
import com.jbd.stock.service.mapper.RoleMapper;
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
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().name(DEFAULT_NAME);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().name(UPDATED_NAME);
        return role;
    }

    @BeforeEach
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getRolesByIdFiltering() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        Long id = role.getId();

        defaultRoleShouldBeFound("id.equals=" + id);
        defaultRoleShouldNotBeFound("id.notEquals=" + id);

        defaultRoleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoleShouldNotBeFound("id.greaterThan=" + id);

        defaultRoleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where name equals to DEFAULT_NAME
        defaultRoleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the roleList where name equals to UPDATED_NAME
        defaultRoleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRoleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the roleList where name equals to UPDATED_NAME
        defaultRoleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where name is not null
        defaultRoleShouldBeFound("name.specified=true");

        // Get all the roleList where name is null
        defaultRoleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRolesByNameContainsSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where name contains DEFAULT_NAME
        defaultRoleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the roleList where name contains UPDATED_NAME
        defaultRoleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where name does not contain DEFAULT_NAME
        defaultRoleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the roleList where name does not contain UPDATED_NAME
        defaultRoleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRolesByUsersIsEqualToSomething() throws Exception {
        Utilisateur users;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            roleRepository.saveAndFlush(role);
            users = UtilisateurResourceIT.createEntity(em);
        } else {
            users = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(users);
        em.flush();
        role.addUsers(users);
        roleRepository.saveAndFlush(role);
        Long usersId = users.getId();

        // Get all the roleList where users equals to usersId
        defaultRoleShouldBeFound("usersId.equals=" + usersId);

        // Get all the roleList where users equals to (usersId + 1)
        defaultRoleShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoleShouldBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoleShouldNotBeFound(String filter) throws Exception {
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole.name(UPDATED_NAME);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.name(UPDATED_NAME);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, role.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
