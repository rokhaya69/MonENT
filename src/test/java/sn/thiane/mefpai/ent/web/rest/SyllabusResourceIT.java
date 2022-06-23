package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.thiane.mefpai.ent.domain.Syllabus;
import sn.thiane.mefpai.ent.repository.SyllabusRepository;

/**
 * Integration tests for the {@link SyllabusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SyllabusResourceIT {

    private static final String DEFAULT_NOM_SYLLABUS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SYLLABUS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SYLLABUS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SYLLABUS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SYLLABUS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SYLLABUS_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/syllabi";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSyllabusMockMvc;

    private Syllabus syllabus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Syllabus createEntity(EntityManager em) {
        Syllabus syllabus = new Syllabus()
            .nomSyllabus(DEFAULT_NOM_SYLLABUS)
            .syllabus(DEFAULT_SYLLABUS)
            .syllabusContentType(DEFAULT_SYLLABUS_CONTENT_TYPE);
        return syllabus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Syllabus createUpdatedEntity(EntityManager em) {
        Syllabus syllabus = new Syllabus()
            .nomSyllabus(UPDATED_NOM_SYLLABUS)
            .syllabus(UPDATED_SYLLABUS)
            .syllabusContentType(UPDATED_SYLLABUS_CONTENT_TYPE);
        return syllabus;
    }

    @BeforeEach
    public void initTest() {
        syllabus = createEntity(em);
    }

    @Test
    @Transactional
    void createSyllabus() throws Exception {
        int databaseSizeBeforeCreate = syllabusRepository.findAll().size();
        // Create the Syllabus
        restSyllabusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(syllabus)))
            .andExpect(status().isCreated());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeCreate + 1);
        Syllabus testSyllabus = syllabusList.get(syllabusList.size() - 1);
        assertThat(testSyllabus.getNomSyllabus()).isEqualTo(DEFAULT_NOM_SYLLABUS);
        assertThat(testSyllabus.getSyllabus()).isEqualTo(DEFAULT_SYLLABUS);
        assertThat(testSyllabus.getSyllabusContentType()).isEqualTo(DEFAULT_SYLLABUS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSyllabusWithExistingId() throws Exception {
        // Create the Syllabus with an existing ID
        syllabus.setId(1L);

        int databaseSizeBeforeCreate = syllabusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSyllabusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(syllabus)))
            .andExpect(status().isBadRequest());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSyllabusIsRequired() throws Exception {
        int databaseSizeBeforeTest = syllabusRepository.findAll().size();
        // set the field null
        syllabus.setNomSyllabus(null);

        // Create the Syllabus, which fails.

        restSyllabusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(syllabus)))
            .andExpect(status().isBadRequest());

        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSyllabi() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        // Get all the syllabusList
        restSyllabusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(syllabus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSyllabus").value(hasItem(DEFAULT_NOM_SYLLABUS)))
            .andExpect(jsonPath("$.[*].syllabusContentType").value(hasItem(DEFAULT_SYLLABUS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].syllabus").value(hasItem(Base64Utils.encodeToString(DEFAULT_SYLLABUS))));
    }

    @Test
    @Transactional
    void getSyllabus() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        // Get the syllabus
        restSyllabusMockMvc
            .perform(get(ENTITY_API_URL_ID, syllabus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(syllabus.getId().intValue()))
            .andExpect(jsonPath("$.nomSyllabus").value(DEFAULT_NOM_SYLLABUS))
            .andExpect(jsonPath("$.syllabusContentType").value(DEFAULT_SYLLABUS_CONTENT_TYPE))
            .andExpect(jsonPath("$.syllabus").value(Base64Utils.encodeToString(DEFAULT_SYLLABUS)));
    }

    @Test
    @Transactional
    void getNonExistingSyllabus() throws Exception {
        // Get the syllabus
        restSyllabusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSyllabus() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();

        // Update the syllabus
        Syllabus updatedSyllabus = syllabusRepository.findById(syllabus.getId()).get();
        // Disconnect from session so that the updates on updatedSyllabus are not directly saved in db
        em.detach(updatedSyllabus);
        updatedSyllabus.nomSyllabus(UPDATED_NOM_SYLLABUS).syllabus(UPDATED_SYLLABUS).syllabusContentType(UPDATED_SYLLABUS_CONTENT_TYPE);

        restSyllabusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSyllabus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSyllabus))
            )
            .andExpect(status().isOk());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
        Syllabus testSyllabus = syllabusList.get(syllabusList.size() - 1);
        assertThat(testSyllabus.getNomSyllabus()).isEqualTo(UPDATED_NOM_SYLLABUS);
        assertThat(testSyllabus.getSyllabus()).isEqualTo(UPDATED_SYLLABUS);
        assertThat(testSyllabus.getSyllabusContentType()).isEqualTo(UPDATED_SYLLABUS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, syllabus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(syllabus))
            )
            .andExpect(status().isBadRequest());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(syllabus))
            )
            .andExpect(status().isBadRequest());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(syllabus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSyllabusWithPatch() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();

        // Update the syllabus using partial update
        Syllabus partialUpdatedSyllabus = new Syllabus();
        partialUpdatedSyllabus.setId(syllabus.getId());

        partialUpdatedSyllabus
            .nomSyllabus(UPDATED_NOM_SYLLABUS)
            .syllabus(UPDATED_SYLLABUS)
            .syllabusContentType(UPDATED_SYLLABUS_CONTENT_TYPE);

        restSyllabusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSyllabus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSyllabus))
            )
            .andExpect(status().isOk());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
        Syllabus testSyllabus = syllabusList.get(syllabusList.size() - 1);
        assertThat(testSyllabus.getNomSyllabus()).isEqualTo(UPDATED_NOM_SYLLABUS);
        assertThat(testSyllabus.getSyllabus()).isEqualTo(UPDATED_SYLLABUS);
        assertThat(testSyllabus.getSyllabusContentType()).isEqualTo(UPDATED_SYLLABUS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSyllabusWithPatch() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();

        // Update the syllabus using partial update
        Syllabus partialUpdatedSyllabus = new Syllabus();
        partialUpdatedSyllabus.setId(syllabus.getId());

        partialUpdatedSyllabus
            .nomSyllabus(UPDATED_NOM_SYLLABUS)
            .syllabus(UPDATED_SYLLABUS)
            .syllabusContentType(UPDATED_SYLLABUS_CONTENT_TYPE);

        restSyllabusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSyllabus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSyllabus))
            )
            .andExpect(status().isOk());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
        Syllabus testSyllabus = syllabusList.get(syllabusList.size() - 1);
        assertThat(testSyllabus.getNomSyllabus()).isEqualTo(UPDATED_NOM_SYLLABUS);
        assertThat(testSyllabus.getSyllabus()).isEqualTo(UPDATED_SYLLABUS);
        assertThat(testSyllabus.getSyllabusContentType()).isEqualTo(UPDATED_SYLLABUS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, syllabus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(syllabus))
            )
            .andExpect(status().isBadRequest());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(syllabus))
            )
            .andExpect(status().isBadRequest());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSyllabus() throws Exception {
        int databaseSizeBeforeUpdate = syllabusRepository.findAll().size();
        syllabus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSyllabusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(syllabus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Syllabus in the database
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSyllabus() throws Exception {
        // Initialize the database
        syllabusRepository.saveAndFlush(syllabus);

        int databaseSizeBeforeDelete = syllabusRepository.findAll().size();

        // Delete the syllabus
        restSyllabusMockMvc
            .perform(delete(ENTITY_API_URL_ID, syllabus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Syllabus> syllabusList = syllabusRepository.findAll();
        assertThat(syllabusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
