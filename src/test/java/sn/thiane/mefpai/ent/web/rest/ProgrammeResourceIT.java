package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.util.Base64Utils;
import sn.thiane.mefpai.ent.IntegrationTest;
import sn.thiane.mefpai.ent.domain.Programme;
import sn.thiane.mefpai.ent.repository.ProgrammeRepository;

/**
 * Integration tests for the {@link ProgrammeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgrammeResourceIT {

    private static final String DEFAULT_NOM_PROGRAM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROGRAM = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENU_PROGRAM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENU_PROGRAM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENU_PROGRAM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENU_PROGRAM_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_ANNEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/programmes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgrammeMockMvc;

    private Programme programme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createEntity(EntityManager em) {
        Programme programme = new Programme()
            .nomProgram(DEFAULT_NOM_PROGRAM)
            .contenuProgram(DEFAULT_CONTENU_PROGRAM)
            .contenuProgramContentType(DEFAULT_CONTENU_PROGRAM_CONTENT_TYPE)
            .annee(DEFAULT_ANNEE);
        return programme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createUpdatedEntity(EntityManager em) {
        Programme programme = new Programme()
            .nomProgram(UPDATED_NOM_PROGRAM)
            .contenuProgram(UPDATED_CONTENU_PROGRAM)
            .contenuProgramContentType(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE)
            .annee(UPDATED_ANNEE);
        return programme;
    }

    @BeforeEach
    public void initTest() {
        programme = createEntity(em);
    }

    @Test
    @Transactional
    void createProgramme() throws Exception {
        int databaseSizeBeforeCreate = programmeRepository.findAll().size();
        // Create the Programme
        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isCreated());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getNomProgram()).isEqualTo(DEFAULT_NOM_PROGRAM);
        assertThat(testProgramme.getContenuProgram()).isEqualTo(DEFAULT_CONTENU_PROGRAM);
        assertThat(testProgramme.getContenuProgramContentType()).isEqualTo(DEFAULT_CONTENU_PROGRAM_CONTENT_TYPE);
        assertThat(testProgramme.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void createProgrammeWithExistingId() throws Exception {
        // Create the Programme with an existing ID
        programme.setId(1L);

        int databaseSizeBeforeCreate = programmeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomProgramIsRequired() throws Exception {
        int databaseSizeBeforeTest = programmeRepository.findAll().size();
        // set the field null
        programme.setNomProgram(null);

        // Create the Programme, which fails.

        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isBadRequest());

        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = programmeRepository.findAll().size();
        // set the field null
        programme.setAnnee(null);

        // Create the Programme, which fails.

        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isBadRequest());

        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProgrammes() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get all the programmeList
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProgram").value(hasItem(DEFAULT_NOM_PROGRAM)))
            .andExpect(jsonPath("$.[*].contenuProgramContentType").value(hasItem(DEFAULT_CONTENU_PROGRAM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenuProgram").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENU_PROGRAM))))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())));
    }

    @Test
    @Transactional
    void getProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get the programme
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, programme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programme.getId().intValue()))
            .andExpect(jsonPath("$.nomProgram").value(DEFAULT_NOM_PROGRAM))
            .andExpect(jsonPath("$.contenuProgramContentType").value(DEFAULT_CONTENU_PROGRAM_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenuProgram").value(Base64Utils.encodeToString(DEFAULT_CONTENU_PROGRAM)))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProgramme() throws Exception {
        // Get the programme
        restProgrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme
        Programme updatedProgramme = programmeRepository.findById(programme.getId()).get();
        // Disconnect from session so that the updates on updatedProgramme are not directly saved in db
        em.detach(updatedProgramme);
        updatedProgramme
            .nomProgram(UPDATED_NOM_PROGRAM)
            .contenuProgram(UPDATED_CONTENU_PROGRAM)
            .contenuProgramContentType(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE)
            .annee(UPDATED_ANNEE);

        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgramme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getNomProgram()).isEqualTo(UPDATED_NOM_PROGRAM);
        assertThat(testProgramme.getContenuProgram()).isEqualTo(UPDATED_CONTENU_PROGRAM);
        assertThat(testProgramme.getContenuProgramContentType()).isEqualTo(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE);
        assertThat(testProgramme.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void putNonExistingProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme
            .nomProgram(UPDATED_NOM_PROGRAM)
            .contenuProgram(UPDATED_CONTENU_PROGRAM)
            .contenuProgramContentType(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getNomProgram()).isEqualTo(UPDATED_NOM_PROGRAM);
        assertThat(testProgramme.getContenuProgram()).isEqualTo(UPDATED_CONTENU_PROGRAM);
        assertThat(testProgramme.getContenuProgramContentType()).isEqualTo(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE);
        assertThat(testProgramme.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme
            .nomProgram(UPDATED_NOM_PROGRAM)
            .contenuProgram(UPDATED_CONTENU_PROGRAM)
            .contenuProgramContentType(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE)
            .annee(UPDATED_ANNEE);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getNomProgram()).isEqualTo(UPDATED_NOM_PROGRAM);
        assertThat(testProgramme.getContenuProgram()).isEqualTo(UPDATED_CONTENU_PROGRAM);
        assertThat(testProgramme.getContenuProgramContentType()).isEqualTo(UPDATED_CONTENU_PROGRAM_CONTENT_TYPE);
        assertThat(testProgramme.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeDelete = programmeRepository.findAll().size();

        // Delete the programme
        restProgrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, programme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
