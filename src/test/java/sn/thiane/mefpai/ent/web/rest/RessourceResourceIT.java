package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;
import sn.thiane.mefpai.ent.IntegrationTest;
import sn.thiane.mefpai.ent.domain.Ressource;
import sn.thiane.mefpai.ent.domain.enumeration.TypeRessource;
import sn.thiane.mefpai.ent.repository.RessourceRepository;

/**
 * Integration tests for the {@link RessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RessourceResourceIT {

    private static final String DEFAULT_LIBEL_RESSOURCE = "AAAAAAAAAA";
    private static final String UPDATED_LIBEL_RESSOURCE = "BBBBBBBBBB";

    private static final TypeRessource DEFAULT_TYPE_RESSOURCE = TypeRessource.SupportCours;
    private static final TypeRessource UPDATED_TYPE_RESSOURCE = TypeRessource.Evaluation;

    private static final byte[] DEFAULT_LIEN_RESSOURCE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LIEN_RESSOURCE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LIEN_RESSOURCE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_MISE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MISE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRessourceMockMvc;

    private Ressource ressource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createEntity(EntityManager em) {
        Ressource ressource = new Ressource()
            .libelRessource(DEFAULT_LIBEL_RESSOURCE)
            .typeRessource(DEFAULT_TYPE_RESSOURCE)
            .lienRessource(DEFAULT_LIEN_RESSOURCE)
            .lienRessourceContentType(DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE)
            .dateMise(DEFAULT_DATE_MISE);
        return ressource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createUpdatedEntity(EntityManager em) {
        Ressource ressource = new Ressource()
            .libelRessource(UPDATED_LIBEL_RESSOURCE)
            .typeRessource(UPDATED_TYPE_RESSOURCE)
            .lienRessource(UPDATED_LIEN_RESSOURCE)
            .lienRessourceContentType(UPDATED_LIEN_RESSOURCE_CONTENT_TYPE)
            .dateMise(UPDATED_DATE_MISE);
        return ressource;
    }

    @BeforeEach
    public void initTest() {
        ressource = createEntity(em);
    }

    @Test
    @Transactional
    void createRessource() throws Exception {
        int databaseSizeBeforeCreate = ressourceRepository.findAll().size();
        // Create the Ressource
        restRessourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ressource)))
            .andExpect(status().isCreated());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate + 1);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelRessource()).isEqualTo(DEFAULT_LIBEL_RESSOURCE);
        assertThat(testRessource.getTypeRessource()).isEqualTo(DEFAULT_TYPE_RESSOURCE);
        assertThat(testRessource.getLienRessource()).isEqualTo(DEFAULT_LIEN_RESSOURCE);
        assertThat(testRessource.getLienRessourceContentType()).isEqualTo(DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE);
        assertThat(testRessource.getDateMise()).isEqualTo(DEFAULT_DATE_MISE);
    }

    @Test
    @Transactional
    void createRessourceWithExistingId() throws Exception {
        // Create the Ressource with an existing ID
        ressource.setId(1L);

        int databaseSizeBeforeCreate = ressourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRessourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ressource)))
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelRessourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressourceRepository.findAll().size();
        // set the field null
        ressource.setLibelRessource(null);

        // Create the Ressource, which fails.

        restRessourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ressource)))
            .andExpect(status().isBadRequest());

        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeRessourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressourceRepository.findAll().size();
        // set the field null
        ressource.setTypeRessource(null);

        // Create the Ressource, which fails.

        restRessourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ressource)))
            .andExpect(status().isBadRequest());

        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRessources() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        // Get all the ressourceList
        restRessourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressource.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelRessource").value(hasItem(DEFAULT_LIBEL_RESSOURCE)))
            .andExpect(jsonPath("$.[*].typeRessource").value(hasItem(DEFAULT_TYPE_RESSOURCE.toString())))
            .andExpect(jsonPath("$.[*].lienRessourceContentType").value(hasItem(DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].lienRessource").value(hasItem(Base64Utils.encodeToString(DEFAULT_LIEN_RESSOURCE))))
            .andExpect(jsonPath("$.[*].dateMise").value(hasItem(DEFAULT_DATE_MISE.toString())));
    }

    @Test
    @Transactional
    void getRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        // Get the ressource
        restRessourceMockMvc
            .perform(get(ENTITY_API_URL_ID, ressource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ressource.getId().intValue()))
            .andExpect(jsonPath("$.libelRessource").value(DEFAULT_LIBEL_RESSOURCE))
            .andExpect(jsonPath("$.typeRessource").value(DEFAULT_TYPE_RESSOURCE.toString()))
            .andExpect(jsonPath("$.lienRessourceContentType").value(DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE))
            .andExpect(jsonPath("$.lienRessource").value(Base64Utils.encodeToString(DEFAULT_LIEN_RESSOURCE)))
            .andExpect(jsonPath("$.dateMise").value(DEFAULT_DATE_MISE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRessource() throws Exception {
        // Get the ressource
        restRessourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource
        Ressource updatedRessource = ressourceRepository.findById(ressource.getId()).get();
        // Disconnect from session so that the updates on updatedRessource are not directly saved in db
        em.detach(updatedRessource);
        updatedRessource
            .libelRessource(UPDATED_LIBEL_RESSOURCE)
            .typeRessource(UPDATED_TYPE_RESSOURCE)
            .lienRessource(UPDATED_LIEN_RESSOURCE)
            .lienRessourceContentType(UPDATED_LIEN_RESSOURCE_CONTENT_TYPE)
            .dateMise(UPDATED_DATE_MISE);

        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRessource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRessource))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelRessource()).isEqualTo(UPDATED_LIBEL_RESSOURCE);
        assertThat(testRessource.getTypeRessource()).isEqualTo(UPDATED_TYPE_RESSOURCE);
        assertThat(testRessource.getLienRessource()).isEqualTo(UPDATED_LIEN_RESSOURCE);
        assertThat(testRessource.getLienRessourceContentType()).isEqualTo(UPDATED_LIEN_RESSOURCE_CONTENT_TYPE);
        assertThat(testRessource.getDateMise()).isEqualTo(UPDATED_DATE_MISE);
    }

    @Test
    @Transactional
    void putNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ressource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ressource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource.dateMise(UPDATED_DATE_MISE);

        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelRessource()).isEqualTo(DEFAULT_LIBEL_RESSOURCE);
        assertThat(testRessource.getTypeRessource()).isEqualTo(DEFAULT_TYPE_RESSOURCE);
        assertThat(testRessource.getLienRessource()).isEqualTo(DEFAULT_LIEN_RESSOURCE);
        assertThat(testRessource.getLienRessourceContentType()).isEqualTo(DEFAULT_LIEN_RESSOURCE_CONTENT_TYPE);
        assertThat(testRessource.getDateMise()).isEqualTo(UPDATED_DATE_MISE);
    }

    @Test
    @Transactional
    void fullUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource
            .libelRessource(UPDATED_LIBEL_RESSOURCE)
            .typeRessource(UPDATED_TYPE_RESSOURCE)
            .lienRessource(UPDATED_LIEN_RESSOURCE)
            .lienRessourceContentType(UPDATED_LIEN_RESSOURCE_CONTENT_TYPE)
            .dateMise(UPDATED_DATE_MISE);

        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelRessource()).isEqualTo(UPDATED_LIBEL_RESSOURCE);
        assertThat(testRessource.getTypeRessource()).isEqualTo(UPDATED_TYPE_RESSOURCE);
        assertThat(testRessource.getLienRessource()).isEqualTo(UPDATED_LIEN_RESSOURCE);
        assertThat(testRessource.getLienRessourceContentType()).isEqualTo(UPDATED_LIEN_RESSOURCE_CONTENT_TYPE);
        assertThat(testRessource.getDateMise()).isEqualTo(UPDATED_DATE_MISE);
    }

    @Test
    @Transactional
    void patchNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ressource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ressource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeDelete = ressourceRepository.findAll().size();

        // Delete the ressource
        restRessourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ressource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
